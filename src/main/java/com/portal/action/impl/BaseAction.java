package com.portal.action.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.action.Action;
import com.portal.action.ActionConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.utils.PortalUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseAction implements Action<Map<String, Object>, JSONObject, String> {
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	protected CandidateFeedback getCandidateFeedback(Map<String, Object> input,  JSONObject workFlow, String action, CandidateFeedbackRepository candidateFeedbackRepository) throws Exception {
		CandidateFeedback candidateFeedback = candidateFeedbackRepository
				.findById((String) input.get(ActionConstants.CANDIDATE_ID))
				.orElseThrow(() -> new UserNotFoundException("Candidate Not Found"));
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		if (canhistorylist == null) {
			candidateFeedback.setCandidateHistory(new ArrayList<>());
		}
		List<String> allowedActions = PortalUtils.jsonArrayToList((JSONArray) ((JSONObject)workFlow.get(candidateFeedback.getStatus())).get("Allowed"));
		if (allowedActions != null && !allowedActions.contains(action)) {
			throw new Exception("Not Allowed Work flow");
		}
		return candidateFeedback;
	}
	
	protected List<String> getNextSteps(JSONObject workFlow, String action){
		return PortalUtils.jsonArrayToList((JSONArray) ((JSONObject)workFlow.get(action)).get("Allowed"));
	}
	
	protected void resetcurrentFeedback(CandidateFeedback candidateFeedback)
	{
		candidateFeedback.setTimeTakenForInterview(null);
		candidateFeedback.setInterviewerName(null);
		candidateFeedback.setCurrentInterviewId(null);
		candidateFeedback.setNextInterviewDate(null);
		candidateFeedback.setNextInterviewTime(null);
		candidateFeedback.setOverAllFeedBack(null);
		candidateFeedback.setEventId(null);
		candidateFeedback.setFeedBack(null);
	}

	/**
	 * Saving the lastmodified date
	 */
	@Override
	public void action(Map<String, Object> input, JSONObject workFlow, String action) throws Exception
	{
		String candidateId = (String) input.get(ActionConstants.CANDIDATE_ID);
		if(candidateId != null)
		{
			Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
			candidateOptional.ifPresent( (candidate) -> 
					{ 
						candidate.setLastModifiedDate(LocalDateTime.now());
						candidateRepository.save(candidate);
					});
		}
	}
}
