package com.portal.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portal.action.ActionConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.Interviewer;
import com.portal.bean.UpdateCandidatePayload;
import com.portal.bean.WorkFlowBean;
import com.portal.excel.CandidateExcelExporter;
import com.portal.exception.CandidatePresentException;
import com.portal.exception.UserNotFoundException;
import com.portal.service.AdminService;
import com.portal.service.CandidateService;
import com.portal.service.InterviewerService;
import com.portal.validations.ValidateEmailPhoneNumberDb;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Validated
public class CandidateController {

	private CandidateService candidateService;

	private InterviewerService interviewerService;

	@Value("${resume.paths}")
	private String path;
	
	private AdminService adminService;


	private static final String DIGITS = "\\d+$";

	/*
	 * public CandidateController() {
	 * 
	 * }
	 */

	public CandidateController(CandidateService candidateService, AdminService adminService,InterviewerService interviewerService) {
		this.candidateService = candidateService;
		this.adminService = adminService;
		this.interviewerService = interviewerService;
	}

	@CrossOrigin(maxAge = 3600,origins = "*")
	@Operation(summary = "This method is used to Create Candidate with File")
	@PostMapping(value = "/candidate/withFile")
	public Candidate createCandidateWithFile(@ModelAttribute @ValidateEmailPhoneNumberDb Candidate candidate, @RequestParam(name = "file", required = false) MultipartFile file, @RequestHeader(required = false, name="isExternalUser") boolean externalUser, @RequestHeader(value="token") String token) throws Exception {
		log.debug("Cadidate With Details : " + candidate);
		Candidate candidateCreated = createCandidateWithOutFile(candidate, token, externalUser);
		updateCandidateResume(file, candidateCreated.getId());
		return candidateCreated;

	}

	private Candidate createCandidateWithOutFile(Candidate candidate, String token, boolean externalUser) throws Exception {
		UUID uuid = UUID.randomUUID();
		candidate.setId(uuid.toString());
		CandidateFeedback canFeedBack = new CandidateFeedback();
		canFeedBack.setId(uuid.toString());
		candidate.setCandidateFeedback(canFeedBack);
		JSONObject jsonObject = candidateService.decodeUserToken(token);
		Interviewer interviewer = interviewerService.getInterviewerById(jsonObject.getString("userId"));
		candidate.setUpLoader(interviewer);

		candidateService.createCandidate(candidate);
		//This helps to move any candidate to Applied stage.
		WorkFlowBean workflow = new WorkFlowBean();
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put(ActionConstants.CANDIDATE_ID, uuid.toString());
		workflow.setValueMap(valueMap);
		adminService.callWorkFlow(workflow, true,null);

		//This helps to move any candidate to Pending short list stage if user is not external and selector is not null.
		if(!externalUser && candidate.getSelectorId() != null) {
			valueMap.put(ActionConstants.SELECTOR_ID, candidate.getSelectorId());
			workflow.setAction("PendingShortList");
			workflow.setValueMap(valueMap);
			adminService.callWorkFlow(workflow, false, null);
		}

		Candidate createdCandidate = candidateService.getCandidateById(uuid.toString());
		//solrIntegrationService.addCandidateDataToSolr(createdCandidate);
		return createdCandidate;
	}
	@CrossOrigin(maxAge = 3600,origins = "*")
	@Operation(summary = "This method is used to Create Candidate without file")
	@PostMapping(value = "/candidate")
	public Candidate createCandidate(@Valid @RequestBody Candidate candidate, @RequestHeader(required = false, name="isExternalUser") boolean externalUser, 
			@RequestHeader(value="token") String token) throws Exception {
		log.debug("Cadidate With Details : " + candidate);
		return createCandidateWithOutFile(candidate, token, externalUser);	
	}

	@Operation(summary = "This method is used to find duplicate email and phonenumber")
	@PostMapping(value = "/candidate/{value}")
	public void findByEmailAndPhoneNumber(@PathVariable(name = "value") String value) throws CandidatePresentException {
		Pattern pattern = Pattern.compile(DIGITS);
		Matcher matcher = pattern.matcher(value);
		Candidate candidateExists = null;
		if (matcher.matches()) {
			candidateExists = candidateService.findByphoneNumber(value);
		} else {
			candidateExists = candidateService.findByEmail(value);
		}
		if (candidateExists != null) {

			throw new CandidatePresentException("Candidate already registered");
		}
	}

	@Operation(summary = "This method is used to Get all Candidates with pagination. Page is current pagenumber, records is no.of records to be returned per page")
	@GetMapping(value = "/candidate/{page}/{records}")
	public Page<Candidate> getAllCandidatesWithPagination(@PathVariable(value = "page") int page, @PathVariable(value = "records") int records,@RequestHeader(value="token") String token) {
		return candidateService.getAllCandidatesWithPagination(page, records, token);

	}

	@Operation(summary = "This method is used to Get all Candidates")
	@GetMapping(value = "/candidate")
	public List<Candidate> getAllCandidates(@RequestHeader(value="token") String token) {
		return candidateService.getAllCandidates(token);

	}

	@Operation(summary = "This method is used to Get get Candidate By Id")
	@GetMapping(value = "/candidate/{id}")
	public Candidate getCandidateId(@PathVariable String id) throws UserNotFoundException {
		return candidateService.getCandidateById(id);
	}

	@GetMapping(value = "/candidate/interviewer/{id}")
	public List<Candidate> getCandidateByInterviewerId(@PathVariable String id) throws UserNotFoundException {
		return candidateService.getCandidateByInterviewerId(id);
	}

	@Operation(summary = "This method is used to Update Candidate By Id")
	@PutMapping(value = "/candidate")
	public Candidate updateCandidateById(@RequestBody UpdateCandidatePayload  updateCandidatePayload) throws Exception {
		return candidateService.updateCandidateById(updateCandidatePayload);
	}

	@Operation(summary = "This method is used to Delete Candidate By Id")
	@DeleteMapping(value = "/candidate/{id}")
	public void deleteCandidateById(@PathVariable(value = "id") String id) {
		candidateService.deleteCandidateById(id);
	}

	@Operation(summary = "This method is used to get list of candidates based on candidate feedback status")
	@PostMapping("/candidate/status")
	public List<Candidate> getListOfCandidatesOnStatus(@RequestBody List<String> status) {
		return candidateService.getAllCandidatesBasedOnstatus(status);
	}

	@GetMapping(value = "/candidate/download/{id}")
	public ResponseEntity getResume(@PathVariable String id) throws UserNotFoundException, MalformedURLException {
		Path filePath = Paths.get(path + candidateService.getCandidateById(id).getResume());
		Resource resource = new UrlResource(filePath.toUri());
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@Operation(summary = "This method is used to Upload Candidate Resume")
	@PostMapping("/candidate/upload/{id}")
	public void uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) throws Exception {
		updateCandidateResume(file, id);
	}
	
	private void updateCandidateResume(MultipartFile file, String id) throws Exception {
		String[] fileFrags = file.getOriginalFilename().split("\\.");
		String resumeName = id + "." + fileFrags[fileFrags.length - 1];
		Candidate  candidate = candidateService.getCandidateById(id);
		if(Files.exists(Paths.get(path + "/" + candidate.getResume())))
			Files.deleteIfExists(Paths.get(path + "/" + candidate.getResume()));
		Path root = Paths.get(path);
		Files.copy(file.getInputStream(), root.resolve(resumeName));
		candidateService.updateCandidateResume(id, fileFrags[fileFrags.length - 1]);
	}
	
	@Operation(summary = "This method is used to Upload Candidate Resume")
	@PostMapping(value="/candidate/upload/{id}/{jobId}", consumes = {"multipart/form-data"})
	public	String uploadFile(@RequestPart("file") MultipartFile file, @PathVariable("id") String id, @PathVariable("jobId") String jobId ) throws Exception {
		return candidateService.updateCandidateResume(file, id,jobId);
	}
	
	/**
	 * The Method will create Excel report for the list of candidates
	 * This will have fields ID,firstName,lastName,phoneNumber,email,
	 * jobTitle,currentCtc,expectedCtc,noticePeriod,resumeUploadedByName,resumeUploadedByEmail,
	 * currentInterviewer,currentStatus,immediateInterviewDate,interviewTime
	 * @return 
	 * @throws IOException
	 * @throws ParseException 
	 */
	@Operation(summary = "This method is used to download the excel report of all candidates")
	@GetMapping(value = "/candidate/export/excel")
	public ResponseEntity<Resource> exportToExcel(@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws IOException, ParseException {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String fileName = "candidates_" + currentDateTime + ".xlsx";

		List<Candidate> listCandidate = null;
		if(startDate != null && endDate != null) {
			listCandidate = candidateService.getAllCandidatesExcel(startDate, endDate);
		}else {
			listCandidate = candidateService.getAllCandidatesExcel();
		}

		CandidateExcelExporter excelExporter = new CandidateExcelExporter(listCandidate);

		InputStreamResource file = new InputStreamResource(excelExporter.export());  

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName).contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}  

}