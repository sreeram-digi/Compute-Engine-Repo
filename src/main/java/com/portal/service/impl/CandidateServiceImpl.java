package com.portal.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.portal.ApplicationConfigurations;
import com.portal.ApplicationConstants;
import com.portal.action.ActionConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.Interviewer;
import com.portal.bean.Job;
import com.portal.bean.MembersForMeeting;
import com.portal.bean.UpdateCandidatePayload;
import com.portal.bean.WorkFlowBean;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.repository.JobRepository;
import com.portal.service.AdminService;
import com.portal.service.CandidateService;
import com.portal.utils.JwtTokenUtil;
import com.portal.utils.PortalUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CandidateServiceImpl implements CandidateService {

	@Autowired
	private CandidateRepository candidateRepository;

	private final Path root = Paths.get("uploads");
	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;

	private AdminService adminService;
	@Autowired
	private InterviewerRepository interviewerRepository;

	@Autowired
	private JobRepository jobRepository;


	private EmailUtil emailUtil;
	@Autowired
	private JSONObject workflow;

	@Value("${resume.paths}")
	private String path;

	@Autowired
	private ApplicationConfigurations applicationConfigurations;
	public CandidateServiceImpl() {

	}

	public CandidateServiceImpl(CandidateRepository candidateRepository,
			CandidateFeedbackRepository candidateFeedbackRepository, AdminService adminService, InterviewerRepository interviewerRepository, EmailUtil emailUtil, @Qualifier("workflow") JSONObject workflow) {
		this.candidateRepository = candidateRepository;
		this.candidateFeedbackRepository = candidateFeedbackRepository;
		this.adminService = adminService;
		this.interviewerRepository = interviewerRepository;
		this.emailUtil = emailUtil;
		this.workflow = workflow;
	}


	public CandidateServiceImpl(CandidateRepository candidateRepository2,
			CandidateFeedbackRepository candidateFeedbackRepository2, AdminService
			adminService2, InterviewerRepository interviewerRepository2, EmailUtil
			emailUtil2) { 
		this.candidateRepository = candidateRepository2;
		this.candidateFeedbackRepository = candidateFeedbackRepository2;
		this.adminService = adminService2; 
		this.interviewerRepository = interviewerRepository2; 
		this.emailUtil = emailUtil2; 
	}

	@Override
	public Candidate createCandidate(Candidate candidate) throws Exception {
		candidate.setLastModifiedDate(LocalDateTime.now());
		candidate.setPointOfContact( ((List<Interviewer>) interviewerRepository.findAllById(candidate.getPointOfContactIds())).stream().filter(x->x.isAdmin()).collect(Collectors.toList()));
		if(candidate.getPointOfContact() == null || candidate.getPointOfContact().size() == 0) {
			throw new Exception("Point of contact should be only admins");
		}
		candidate.setUploadedDate(LocalDateTime.now());
		return candidateRepository.save(candidate);
	}

	@Override
	public Page<Candidate> getAllCandidatesWithPagination(int pageNumber, int limit, String token) {	

		Pageable pageable = PageRequest.of(pageNumber-1, limit);
		JSONObject jsonObject = decodeUserToken(token);
		String userId = jsonObject.getString("userId");
		String type = jsonObject.getString("type");
		if(type.equalsIgnoreCase("externalUser")) {
			return candidateRepository.findAllByUploadedByWithPagination(userId, pageable);
		}
		return candidateRepository.findAllWithPagination(pageable);
	}

	public List<Candidate> getAllCandidates(String token){
		JSONObject jsonObject = decodeUserToken(token);
		String userId = jsonObject.getString("userId");
		String type = jsonObject.getString("type");
		if(type.equalsIgnoreCase("externalUser")) {
			return candidateRepository.findAllUploadedBy(userId);

		}

		return candidateRepository.findAll();
	}

	public JSONObject decodeUserToken(String token) {
		return JwtTokenUtil.decodeUserToken(token);
	}

	@Override
	public Candidate getCandidateById(String id) throws UserNotFoundException {
		Candidate candidate = candidateRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with Id " + id + " not found"));

		CandidateFeedback candidateFeedback = candidateFeedbackRepository.findById(id).orElse(new CandidateFeedback());
		if(StringUtils.isNotEmpty(candidateFeedback.getStatus())&&!PortalUtils.jsonArrayToList((JSONArray) ((JSONObject)workflow.get(candidateFeedback.getStatus())).get("Allowed")).isEmpty())
		{			
			candidateFeedback.setNextSteps(PortalUtils.jsonArrayToList((JSONArray) ((JSONObject)workflow.get(candidateFeedback.getStatus())).get("Allowed")));
		}
		candidate.setCandidateFeedback(candidateFeedback);
		return candidate;
	}

	@Override
	public Candidate updateCandidateById(UpdateCandidatePayload updateCandidatePayload) throws Exception {
		Candidate candidate = candidateRepository.findById(updateCandidatePayload.getId())
				.orElseThrow(() -> new UserNotFoundException("User with Id " + updateCandidatePayload.getId() + " not found"));
		if(updateCandidatePayload.getFirstName()!= null)
			candidate.setFirstName(updateCandidatePayload.getFirstName());
		if(updateCandidatePayload.getLastName()!=null)
			candidate.setLastName(updateCandidatePayload.getLastName());
		if(updateCandidatePayload.getPhoneNumber()!=null)
			candidate.setPhoneNumber(updateCandidatePayload.getPhoneNumber());
		if(updateCandidatePayload.getEmail()!=null)
			candidate.setEmail(updateCandidatePayload.getEmail());
		if(updateCandidatePayload.getSkills()!=null)
			candidate.setSkills(updateCandidatePayload.getSkills());
		if(updateCandidatePayload.getJobDescription()!=null)
			candidate.setJobDescription(updateCandidatePayload.getJobDescription());
		if(updateCandidatePayload.getJobTitle()!=null)
			candidate.setJobTitle(updateCandidatePayload.getJobTitle());
		if(updateCandidatePayload.getExperience()>0)
			candidate.setExperience(updateCandidatePayload.getExperience());
		if(updateCandidatePayload.getRelavantExperience()>0)
			candidate.setRelavantExperience(updateCandidatePayload.getRelavantExperience());
		if(updateCandidatePayload.getSelectorId()!=null) {
			candidate.setSelectorId(updateCandidatePayload.getSelectorId());
			Optional<CandidateFeedback> optionalFeedBack = candidateFeedbackRepository.findById(updateCandidatePayload.getId());
			Interviewer interviewer = interviewerRepository.findById(updateCandidatePayload.getSelectorId()).orElseThrow(()->new Exception());
			if(optionalFeedBack.isPresent()) {
				CandidateFeedback canFeedBack = optionalFeedBack.get();
				canFeedBack.setInterviewerName(interviewer.getInterviewerName());
				canFeedBack.setCurrentInterviewId(updateCandidatePayload.getSelectorId());
				candidateFeedbackRepository.save(canFeedBack);
			}
		}
		if(updateCandidatePayload.getUpLoadedBy()!=null)
			candidate.setUpLoadedBy(updateCandidatePayload.getUpLoadedBy());
		candidate.setLastModifiedDate(LocalDateTime.now());
		if(candidateFeedbackRepository.findById(updateCandidatePayload.getId())==null 
				&& StringUtils.isNoneBlank(updateCandidatePayload.getSelectorId())) {
			WorkFlowBean workflow = new WorkFlowBean();
			Map<String, Object> valueMap = new HashMap<>();
			valueMap.put(ActionConstants.CANDIDATE_ID, updateCandidatePayload.getId());
			valueMap.put(ActionConstants.SELECTOR_ID, candidate.getSelectorId());
			workflow.setValueMap(valueMap);
			adminService.callWorkFlow(workflow, true,null);
			CandidateFeedback canfeedBack = candidateFeedbackRepository.findById(updateCandidatePayload.getId()).orElseThrow(()->new Exception("Candidate history not found"));
			candidate.setCandidateFeedback(canfeedBack);
		}
		this.candidateRepository.save(candidate);
		return candidate;
	}

	@Override
	public void deleteCandidateById(String id) {
		candidateRepository.deleteById(id);
	}

	@Override
	public Candidate findByEmail(String value) {
		return candidateRepository.findByEmail(value);
	}

	@Override
	public Candidate findByphoneNumber(String value) {
		return candidateRepository.findByphoneNumber(value);
	}

	@Override
	public List<Candidate> getCandidateByInterviewerId(String currentInterviewId) {
		List<CandidateFeedback> feedbackIds = candidateFeedbackRepository
				.getCandidateByInterviewerId(currentInterviewId);
		List<String> ids = feedbackIds.stream().map(CandidateFeedback::getId).collect(Collectors.toList());
		return (List<Candidate>) candidateRepository.findAllById(ids);

	}

	@Override
	public List<Candidate> getAllCandidatesBasedOnstatus(List<String> status) {
		List<CandidateFeedback> feedBack = candidateFeedbackRepository.findAllBystatus(status);
		return (List<Candidate>) candidateRepository
				.findAllById(feedBack.stream().map(CandidateFeedback::getId).collect(Collectors.toList()));
	}

	@Override
	public void updateCandidateResume(String id, String type) throws UserNotFoundException {
		Candidate candidate = candidateRepository.findById(id).orElseThrow(()-> new UserNotFoundException("Candidate not found"));
		candidate.setResume(id+"."+type);
		candidateRepository.save(candidate);

	}

	@Override
	public void sendNotificationToAssignSelector(JSONObject jsonObject, @Valid Candidate candidate) {

		List<MembersForMeeting> emailNotificationList =new ArrayList<>();
		MembersForMeeting candidateMember = new MembersForMeeting();
		candidateMember.setEmail(applicationConfigurations.getAdminNotificationEmail());
		emailNotificationList.add(candidateMember);

		String subject = emailUtil.constructEmailSubject(ApplicationConstants.ASSIGN_SELECTOR, candidate.getFirstName()+" "+candidate.getLastName());

		String emailbody = emailUtil.constructEmailBody(ApplicationConstants.ASSIGN_SELECTOR, 
				candidate.getFirstName()+" "+candidate.getLastName(),
				Optional.ofNullable(jsonObject.getString("userName")).orElse("") ,
				applicationConfigurations.getPortalURL()+"/viewcandidate/"+candidate.getId());

		adminService.sendNotification(subject,emailbody,emailNotificationList);
	}

	@Override
	public List<Candidate> getAllCandidatesExcel() {
		return candidateRepository.findAll();
	}

	@Override
	public List<Candidate> getAllCandidatesExcel(String startDate, String endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start=sdf.parse(startDate); 
		Date end=sdf.parse(endDate); 
		return candidateRepository.getAllBetweenDates(start, end);
	}

	@Override
	public List<Candidate> getAllCandidateByJobId(String joId) throws Exception {

		List<Candidate> candidateList = candidateRepository.findByJobId(joId) ;

		if(candidateList!=null) {
			return candidateList;
		}
		return candidateList;
	}



	/**
	 * @author Naga Sreeram
	 * {@summary : Validating Skill set from job and throwing Resumes according to the SkillSet}
	 * @exception Exception
	 */
	@Override
	public String  updateCandidateResume(MultipartFile file, String id, String jobId) throws Exception {

		Candidate candidate = candidateRepository.findById(id).orElseThrow(()-> new UserNotFoundException("Candidate not found"));
		Job adminJobDescription = jobRepository.findById(jobId).orElseThrow(()-> new Exception("Job id not found"));

		List<String> arrayList = Arrays.asList(adminJobDescription.getSkillSet().split(","));
		String candidateSkillSet = candidate.getSkills();

		String[] fileFrags = file.getOriginalFilename().split("\\.");
		String resumeName = id + "." + fileFrags[fileFrags.length - 1];

		File sourceFile = new File(path);

		for(int i=0; i<arrayList.size(); i++) {

			if (sourceFile.exists()) {
				String destinationFolderPath = getDestinationFolderPath(arrayList.get(i));

				if (destinationFolderPath != null) {
					File destinationFolder = new File(destinationFolderPath);
					System.out.println(destinationFolder.getAbsolutePath() +"   "+destinationFolder.getPath());
					if (destinationFolder.exists() && destinationFolder.isDirectory()) {

						CandidateFeedback candidateFeedback = candidateFeedbackRepository.findById(id).orElse(new CandidateFeedback());
						if(StringUtils.isNotEmpty(candidateFeedback.getStatus())&&!PortalUtils.jsonArrayToList((JSONArray) ((JSONObject)
								workflow.get(candidateFeedback.getStatus())).get("Allowed")).isEmpty()){	
							candidateFeedback.setNextSteps(PortalUtils.jsonArrayToList((JSONArray) ((JSONObject)workflow.get(candidateFeedback.getStatus())).get("Allowed")));
						}
						candidate.setCandidateFeedback(candidateFeedback);

						if(Files.exists(Paths.get(path + "/" + candidate.getResume())))
							Files.deleteIfExists(Paths.get(path + "/" + candidate.getResume()));
						Path root = Paths.get(destinationFolderPath);
						Files.copy(file.getInputStream(), root.resolve(resumeName));

					}
					else {
						throw new Exception("Destination folder does not exist.");
					}
				} 
				else {
					throw new Exception("Invalid dropdown value.");
				}
			} 
			else {
				throw new Exception("Source file does not exist.");
			}
		}

		candidate.setResume(id+"."+fileFrags[fileFrags.length - 1]);
		candidate.setJobId(jobId);
		System.out.println("job id"+candidate.getId());
		candidateRepository.save(candidate);

		return "operation comepleted";		

	}

	private String getDestinationFolderPath(String folderPath) {
		String destinationFolderPath = null;
		destinationFolderPath = path+"/"+folderPath;
		return destinationFolderPath;
	}

	/**
	 * @author Naga Sreeram
	 * {@summary : When Admin's gives Criteria from dropdown this method provides Values of X and Y axis}
	 */
	@Override
	public Map<String, Integer> getCandidateBySelectedWorkflowStatus(String inputDropdownCriteria) {

		Map<String,Integer> storageForXandYaxisPlottingValues = new HashMap<>();

		String[] placeHondersForDashBoardsGraphsSplitInArray = inputDropdownCriteria.split(",");

		for(int i=0; i<placeHondersForDashBoardsGraphsSplitInArray.length; i++) {
			List<CandidateFeedback> list = candidateFeedbackRepository.findBystatus(placeHondersForDashBoardsGraphsSplitInArray[i]);
			storageForXandYaxisPlottingValues.put(placeHondersForDashBoardsGraphsSplitInArray[i],list.size());
		}

		return storageForXandYaxisPlottingValues;
	}

	/**
	 * @author Naga Sreeram
	 * {@summary : When Admin's clicks on a specific bar , he can view all the data of that specific bar}
	 */
	@Override
	public List<Candidate> listOfCandidatesForSpecificSelectedWorkFLowStatus(String inputStatusCriteria) {

		List<Candidate> filteredCandidateDateFromInputStatuesCriteria = new ArrayList<>();

		List<CandidateFeedback> gettingCandidateIDusingCandidateStatusCriteria =
				candidateFeedbackRepository.findBystatus(inputStatusCriteria);

		List<String> filteringCanidateID = gettingCandidateIDusingCandidateStatusCriteria.stream().map(CandidateFeedback::getId).toList();

		for(int i=0; i<filteringCanidateID.size(); i++) {
			Candidate candidateObject = candidateRepository.findById(filteringCanidateID.get(i)).get();
			filteredCandidateDateFromInputStatuesCriteria.add(candidateObject);
		}

		return filteredCandidateDateFromInputStatuesCriteria;
	}

	@Override
	public Map<String,Integer> getCandidateByRatings(String rating) {

		List<String> groupOfRatingsInput= Arrays.asList(rating.split(","));

		List<CandidateFeedback> candidateFeedbacksList=candidateFeedbackRepository.findAll();

		List<Integer> listOfAverage= new ArrayList<>();

		Map<String,Integer> numberOfCandidatesWithRatings= new HashMap<>();

		List<Map<String,Object>> listOffeedbackRating	=candidateFeedbacksList.stream()
				.map(CandidateFeedback::getFeedBack)
				.filter(p->p !=null && !p.values().contains(null) )
				.collect(Collectors.toList());


		for(Map<String,Object> candidateFeedbackObject : listOffeedbackRating) {

			List<Double> parsingDoubleObjectList= new ArrayList<>();
			String feedback =candidateFeedbackObject.values().toString();	
			for(String indivisualFeeback:feedback.split(", ")) {

				indivisualFeeback=indivisualFeeback.replaceAll("[\\[\\],\\s]", "");
				parsingDoubleObjectList.add(Double.parseDouble(indivisualFeeback));

			}
			OptionalDouble calculatedAverage=parsingDoubleObjectList.stream().mapToDouble(values->values).average();

			listOfAverage.add((int)Math.round(calculatedAverage.getAsDouble()));

		}

		for(String indivisualRating:groupOfRatingsInput) {

			if(listOfAverage.contains(Integer.parseInt(indivisualRating))) {

				List<Integer> noumberOfCandidates=	listOfAverage.stream()
						.filter(p->p.equals(Integer.parseInt(indivisualRating))).toList();	

				numberOfCandidatesWithRatings.put(indivisualRating,noumberOfCandidates.size());
			}
		}

		return numberOfCandidatesWithRatings;
	}




}