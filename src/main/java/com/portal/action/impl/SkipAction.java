package com.portal.action.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.ApplicationConstants;
import com.portal.action.ActionConstants;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.bean.Interviewer;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.service.InterviewerService;
import com.portal.utils.JwtTokenUtil;
import com.portal.utils.PortalUtils;

public class SkipAction extends BaseAction {
	
	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Autowired
	private InterviewerService interviewerService;
	
	@Override
	public void action(Map<String, Object> input, JSONObject workFlow, String action) throws Exception {
	
		CandidateFeedback candidateFeedback = candidateFeedbackRepository
				.findById((String) input.get(ActionConstants.CANDIDATE_ID))
				.orElseThrow(() -> new UserNotFoundException("Candidate Not Found"));
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		if (canhistorylist == null) {
			candidateFeedback.setCandidateHistory(new ArrayList<>());
		}
		canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));
		
		JSONObject currentNode =  ((JSONObject)workFlow.get(candidateFeedback.getStatus()));
		if(currentNode.has("skipAction")) {
			String skipToAction = currentNode.getString("skipAction");
			String token = (String) input.get(ApplicationConstants.JWT_TOKEN);
			JSONObject jwt = JwtTokenUtil.decodeUserToken(token);
			candidateFeedback.setStatus(skipToAction);
			Map<String, Object> map = new HashMap<>();
			Interviewer interviewer = interviewerService.getInterviewerById(jwt.getString("userId"));
			map.put("SkipedById", interviewer.getId());
			map.put("SkipedByName", interviewer.getInterviewerName());
			map.put("SkipedReason", input.get(ActionConstants.OVERALL_FEED_BACK));
			candidateFeedback.setFeedBack(map);
			
			candidateFeedback.setNextInterviewDate(null);
			candidateFeedback.setNextInterviewTime(null);
			candidateFeedback.setTimeTakenForInterview(null);
			candidateFeedback.setCurrentInterviewId(null);
			candidateFeedback.setInterviewerName(null);
			candidateFeedbackRepository.save(candidateFeedback);
			super.action(input, workFlow, skipToAction);
			
		}else {
			throw new Exception("Skip Action is not allowed here");
		}
	}
}
