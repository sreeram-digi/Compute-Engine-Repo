package com.portal.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.portal.bean.Interviewer;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.InterviewerRepository;
import com.portal.service.InterviewerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InterviewerServiceimpl implements InterviewerService {

	private InterviewerRepository interviewerRepository;

	public InterviewerServiceimpl(InterviewerRepository interviewerRepository) {
		this.interviewerRepository = interviewerRepository;
	}

	public Page<Interviewer> getAllInterviewers(int pageNumber, int limit) {
		log.debug("getAllInterviewers(): getting all the interviewers based on pagesize : ");
		
		Pageable pageable = PageRequest.of(pageNumber-1, limit);
		
		return interviewerRepository.findAll(pageable);
	}
	
	@Override
	public List<Interviewer> getAllInterviewers() {
		log.debug("getAllInterviewers(): getting all the interviewers : ");
		return interviewerRepository.findAll();
	}
	
	public Interviewer saveInterviewer(Interviewer interviewer) {
		log.debug("saveInterviewer(): saving the interviewer : " + interviewer);
		/* Converting to lowercase */
		interviewer.setInterviewerEmail(interviewer.getInterviewerEmail().toLowerCase());
		return this.interviewerRepository.save(interviewer);
	}

	public Interviewer updateInterviewer(Interviewer interviewer) throws UserNotFoundException {
		log.debug("updateInterviewer(): updating the interviewer : " + interviewer);
		interviewerRepository.findById(interviewer.getId()).orElseThrow(
				() -> new UserNotFoundException("Interviewer with Id " + interviewer.getId() + " not found"));
		this.interviewerRepository.save(interviewer);
		return interviewer;
	}

	public Interviewer getInterviewerById(String id) throws UserNotFoundException {
		log.debug("getInterviewerById(): getting interviewer with id: " + id);
		return interviewerRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Interviewer with Id " + id + " not found"));
	}

	public void deleteInterviewerId(String id) {
		log.debug("deleteInterviewerId(): deleting interviewer with id: " + id);
		interviewerRepository.deleteById(id);
		log.debug("deleteInterviewerId():deleted the interviewer by id:" + id);

	}

	public List<Interviewer> getAllSelectors() {
		return interviewerRepository.findBySelector(true);
	}

	@Override
	public List<Interviewer> getAllAdmins() {
		return interviewerRepository.findByAdmin(true);
	}

	@Override
	public List<Interviewer> getAllhrs() {
		return interviewerRepository.findByHr(true);
	}
}
