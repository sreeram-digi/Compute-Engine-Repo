package com.portal.action.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.action.ActionConstants;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.utils.PortalUtils;

public class AcceptedOfferAction extends BaseAction{
	
	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;

	@Override
	public void action(Map<String, Object> input, JSONObject workFlowNode, String action) throws Exception {
		
			CandidateFeedback candidateFeedback = candidateFeedbackRepository.findById((String)input.get("candidateId")).orElseThrow(() -> new UserNotFoundException("Candidate Not Found"));
			List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
			if(canhistorylist == null) {
				canhistorylist = new ArrayList<>();
			}
			List<String> allowedActions = PortalUtils.jsonArrayToList((JSONArray) ((JSONObject)workFlowNode.get(candidateFeedback.getStatus())).get("Allowed"));
			if (allowedActions != null && !allowedActions.contains(action)) {
				throw new Exception("Not Allowed Work flow");
			}
			String candidateId = (String) input.get(ActionConstants.CANDIDATE_ID);
			canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));

			candidateFeedback.setId(candidateId);
			candidateFeedback.setStatus(action);
			candidateFeedback.setFeedBack(null);
			candidateFeedback.setOverAllFeedBack((String)input.get(ActionConstants.OVERALL_FEED_BACK));
			candidateFeedback.setCurrentInterviewId(null);
			candidateFeedback.setInterviewerName(null);
			candidateFeedback.setCandidateHistory(canhistorylist);
			candidateFeedbackRepository.save(candidateFeedback);
			
			super.action(input, workFlowNode, action);
	}
}
