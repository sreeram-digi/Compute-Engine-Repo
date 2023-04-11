package com.portal.action.impl;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.action.ActionConstants;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.utils.PortalUtils;

public class NotJoinedAction extends BaseAction {

	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;

	@Override
	public void action(Map<String, Object> input, JSONObject workFlowNode, String action) throws Exception {

		CandidateFeedback candidateFeedback = getCandidateFeedback(input,workFlowNode, action, candidateFeedbackRepository);
		String candidateId = (String) input.get(ActionConstants.CANDIDATE_ID);
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));

		candidateFeedback.setId(candidateId);
		candidateFeedback.setOverAllFeedBack((String) input.get(ActionConstants.OVERALL_FEED_BACK));
		candidateFeedback.setStatus(action);
		candidateFeedback.setCandidateHistory(canhistorylist);
		candidateFeedback.setCurrentInterviewId(null);
		candidateFeedback.setInterviewerName(null);
		candidateFeedbackRepository.save(candidateFeedback);
		
		super.action(input, workFlowNode, action);
	}
}
