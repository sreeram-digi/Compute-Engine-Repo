package com.portal.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.portal.bean.Interviewer;
import com.portal.exception.UserNotFoundException;

public interface InterviewerService {

	Page<Interviewer> getAllInterviewers(int pageNumber, int limit);

	Interviewer saveInterviewer(Interviewer interviewer);

	Interviewer getInterviewerById(String id) throws UserNotFoundException;

	void deleteInterviewerId(String id);

	Interviewer updateInterviewer(Interviewer interviewer) throws UserNotFoundException;

	List<Interviewer> getAllSelectors();
	
	List<Interviewer> getAllAdmins();
	
	List<Interviewer> getAllhrs();

	List<Interviewer> getAllInterviewers();
}
