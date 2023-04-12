package com.portal.action.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.ApplicationConstants;
import com.portal.action.ActionConstants;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.CandidateHistory;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.service.AdminService;
import com.portal.utils.PortalUtils;

public class DroppedAction extends BaseAction {
	
	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Autowired
	private AdminService adminService;
	
	@Override
	public void action(Map<String, Object> input, JSONObject workFlow, String action) throws Exception {
		
		CandidateFeedback candidateFeedback = candidateFeedbackRepository
				.findById((String) input.get(ActionConstants.CANDIDATE_ID))
				.orElseThrow(() -> new UserNotFoundException("Candidate Not Found"));
		if(candidateFeedback.getStatus().contains("Dropped")) {
			throw new Exception("Not allowed to next stage");
		}
		if(!action.equals(candidateFeedback.getStatus())) {
			throw new Exception("Status do not match");
		}
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		if (canhistorylist == null) {
			candidateFeedback.setCandidateHistory(new ArrayList<>());
		}
		
		if (candidateFeedback.getEventId() != null) {
			adminService.deleteEvent(candidateFeedback.getEventId(),(String)input.get(ApplicationConstants.JWT_TOKEN));
		}
		canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));
		candidateFeedback.setStatus("Dropped");
		candidateFeedback.setCurrentInterviewId(null);
		candidateFeedback.setInterviewerName(null);
		candidateFeedbackRepository.save(candidateFeedback);
		
		super.action(input, workFlow, action);
	}

}
