package com.portal.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bean.Interviewer;
import com.portal.exception.UserNotFoundException;
import com.portal.service.InterviewerService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600, origins = "*")
public class InterviewerController {

	private InterviewerService interviewerService;

	public InterviewerController(InterviewerService interviewerservice) {
		this.interviewerService = interviewerservice;
	}
	@Operation(summary = " This method is used to Createinterviewer")
	@PostMapping("/interviewer")
	public Interviewer createInterviewer(@Valid @RequestBody Interviewer interviewer) {
		if(interviewer.getId()==null) {
			UUID uuId= UUID.randomUUID();
			interviewer.setId(uuId.toString());
		}
		log.debug("createInterviewer() :creating the interviewer:" + interviewer.getId());
		return interviewerService.saveInterviewer(interviewer);
	}
	@Operation(summary = " This method is used to get all interviewers based on pagesize")
	@GetMapping("/interviewer/{page}/{records}")
	public Page<Interviewer> getAllInterviewers(@PathVariable(value = "page") int page, @PathVariable(value = "records") int records){
		log.debug("getAllInterviewers() :display all the interviewers based on pagesize:");
		return this.interviewerService.getAllInterviewers(page, records);
	}
	
	@Operation(summary = " This method is used to get all interviewers")
	@GetMapping("/interviewer")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<Interviewer> getAllInterviewers(){
		log.debug("getAllInterviewers() :display all the interviewers:");
		return interviewerService.getAllInterviewers();
	}

	@Operation(summary = " This method is used to get all interviewers")
	@GetMapping("/interviewer/{id}")
	public Interviewer getInterviewerById(@PathVariable(value = "id") String id) throws UserNotFoundException {
		return interviewerService.getInterviewerById(id);
	}

	@Operation(summary = " This method is used to update interviewer by id")
	@PutMapping("/interviewer")
	public Interviewer updatInterviewer(@RequestBody Interviewer interviewer) throws UserNotFoundException {
		log.debug("updatInterviewer():updating the interviewer fields:" + interviewer);
		return this.interviewerService.updateInterviewer(interviewer);
	}

	@Operation(summary = " This method is used to get all interviewers")
	@DeleteMapping("/interviewer/{id}")
	public void deleteInterviewer(@PathVariable(value = "id") String id) {
		log.debug("deleteInterviewer():deleteing the interviewer by id " + id);
		interviewerService.deleteInterviewerId(id);
	}
	@Operation(summary = " This method is used to get all selectors")
	@GetMapping("/interviewer/selectors")
	public List<Interviewer> getSelectorList() {
		return interviewerService.getAllSelectors();
	}
	@Operation(summary = " This method is used to get all admins")
	@GetMapping("/interviewer/admins")
	public List<Interviewer> getAdminList() {
		return interviewerService.getAllAdmins();
	}
	
	@Operation(summary = " This method is used to get all admins")
	@GetMapping("/interviewer/hrs")
	public List<Interviewer> getHrList() {
		return interviewerService.getAllhrs();
	}
}