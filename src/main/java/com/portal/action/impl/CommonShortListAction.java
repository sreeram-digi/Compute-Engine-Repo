package com.portal.action.impl;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.action.ActionConstants;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.utils.PortalUtils;

public class CommonShortListAction extends BaseAction {
	
	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Autowired
	private InterviewerRepository interviewerRepository;

	@Override
	public void action(Map<String, Object> input, JSONObject workFlowNode, String action) throws Exception {
			
			CandidateFeedback candidateFeedback = getCandidateFeedback(input, workFlowNode, action, candidateFeedbackRepository);
			String candidateId = (String) input.get(ActionConstants.CANDIDATE_ID);
			String interViewerId = (String) input.get(ActionConstants.INTERVIEWER_ID);
			String interViewerName = (String) input.get(ActionConstants.INTERVIEWER_NAME);
			List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
			canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));

			candidateFeedback.setId(candidateId);
			candidateFeedback.setCurrentInterviewId(interViewerId);
			candidateFeedback.setInterviewerName(interViewerName);
			candidateFeedback.setFeedBack((Map)input.get(ActionConstants.FEEDBACK));
			candidateFeedback.setOverAllFeedBack((String)input.get(ActionConstants.OVERALL_FEED_BACK));
			candidateFeedback.setStatus(action);
			candidateFeedback.setCandidateHistory(canhistorylist);
			candidateFeedbackRepository.save(candidateFeedback);
			
			super.action(input, workFlowNode, action);
	}
}
