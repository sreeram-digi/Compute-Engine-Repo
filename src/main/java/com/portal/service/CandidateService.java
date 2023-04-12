package com.portal.service;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.portal.bean.Candidate;
import com.portal.bean.UpdateCandidatePayload;
import com.portal.exception.UserNotFoundException;

public interface CandidateService {
	
	Candidate createCandidate(Candidate candidate) throws Exception;

	Page<Candidate> getAllCandidatesWithPagination(int pageNumber, int limit, String token);

	Candidate getCandidateById(String id) throws UserNotFoundException;

	Candidate updateCandidateById(UpdateCandidatePayload updateCandidatePayload) throws Exception;

	void deleteCandidateById(String id);

	Candidate findByEmail(String value);

	Candidate findByphoneNumber(String value);

	List<Candidate> getCandidateByInterviewerId(String currentInterviewId);
	
	List<Candidate> getAllCandidatesBasedOnstatus(List<String> status);
	
	void updateCandidateResume(String id, String fileType) throws UserNotFoundException;
	
	JSONObject decodeUserToken(String token);
	
	List<Candidate> getAllCandidates(String token);

	void sendNotificationToAssignSelector(JSONObject jsonObject, @Valid Candidate candidate);

	List<Candidate> getAllCandidatesExcel();
	
	List<Candidate> getAllCandidatesExcel(String startDate, String endDate) throws ParseException;
}
