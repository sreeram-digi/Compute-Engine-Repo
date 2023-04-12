package com.portal.action.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.ApplicationConstants;
import com.portal.action.ActionConstants;
import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.bean.Interviewer;
import com.portal.bean.MembersForMeeting;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.service.AdminService;
import com.portal.service.impl.EmailUtil;
import com.portal.utils.PortalUtils;

public class ReEvaluteAction extends BaseAction {

	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private InterviewerRepository interviewerRepository;
	
	@Autowired
	private AdminService adminService;

	@Autowired
	private EmailUtil emailUtil;

	@Override
	public void action(Map<String, Object> input, JSONObject workFlow, String action) throws Exception {

		CandidateFeedback candidateFeedback = getCandidateFeedback(input,workFlow, action, candidateFeedbackRepository);
		String candidateId = (String) input.get(ActionConstants.CANDIDATE_ID);
		String interViewerId = (String) input.get(ActionConstants.INTERVIEWER_ID);
		
		
		
		Interviewer interviewer = interviewerRepository.findById(interViewerId).orElseThrow(()->new Exception("Interviewr not found"));
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));

		Candidate candiate = candidateRepository.findById(candidateId).get();
		
		
		JSONObject workFlowNode = workFlow.getJSONObject(action);
		
		
		candidateFeedback.setId(candidateId);
		candidateFeedback.setCurrentInterviewId(interViewerId);
		candidateFeedback.setInterviewerName(interviewer.getInterviewerName());
		candidateFeedback.setFeedBack((Map) input.get(ActionConstants.FEEDBACK));
		candidateFeedback.setOverAllFeedBack((String) input.get(ActionConstants.OVERALL_FEED_BACK));
		candidateFeedback.setStatus(action);
		candidateFeedback.setNextInterviewDate(null);
		candidateFeedback.setNextInterviewTime(null);
		candidateFeedback.setCandidateHistory(canhistorylist);
		candidateFeedbackRepository.save(candidateFeedback);
		
		super.action(input, workFlowNode, action);

	}

}
