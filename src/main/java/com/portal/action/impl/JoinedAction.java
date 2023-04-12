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

public class JoinedAction extends BaseAction {

	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;


	@Override
	public void action(Map<String, Object> input, JSONObject workFlowNode, String action) throws Exception {

		CandidateFeedback candidateFeedback = getCandidateFeedback(input,workFlowNode, action, candidateFeedbackRepository);
		String candidateId = (String) input.get(ActionConstants.CANDIDATE_ID);
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));

		candidateFeedback.setId(candidateId);
		candidateFeedback.setNextInterviewDate(null);
		candidateFeedback.setNextInterviewTime(null);
		candidateFeedback.setTimeTakenForInterview(null);
		candidateFeedback.setCurrentInterviewId(null);
		candidateFeedback.setInterviewerName(null);
		candidateFeedback.setStatus(action);
		candidateFeedback.setFeedBack(null);
		candidateFeedback.setOverAllFeedBack(null);
		candidateFeedback.setCandidateHistory(canhistorylist);
		candidateFeedbackRepository.save(candidateFeedback);
		
		super.action(input, workFlowNode, action);

	}

}
