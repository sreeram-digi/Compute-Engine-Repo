package com.portal.action.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
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

public class ReInitiateAction extends BaseAction {
	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Autowired
	private AdminService adminService;

	@Override
	public void action(Map<String, Object> input, JSONObject workFlow, String action) throws Exception {

		
		CandidateFeedback candidateFeedback = candidateFeedbackRepository
				.findById((String) input.get(ActionConstants.CANDIDATE_ID))
				.orElseThrow(() -> new UserNotFoundException("Candidate Not Found"));
		List<CandidateHistory> canhistorylist = candidateFeedback.getCandidateHistory();
		if (canhistorylist == null) {
			candidateFeedback.setCandidateHistory(new ArrayList<>());
		}
		if (candidateFeedback.getEventId() != null) {
			adminService.deleteEvent(candidateFeedback.getEventId(),(String)input.get(ApplicationConstants.JWT_TOKEN));
		}
		String[] statusSplit = candidateFeedback.getStatus().split("-");
		if(statusSplit.length <2 || (statusSplit.length == 2 && !statusSplit[1].equalsIgnoreCase("OnHold"))) {
			throw new Exception("User is not in hold stage");
		}
		JSONObject node = (JSONObject) workFlow.get(statusSplit[0]);
		canhistorylist.add(PortalUtils.getCanHistory(candidateFeedback,input));
		candidateFeedback.setStatus(statusSplit[0]);
		JSONArray jsonArray = node.getJSONArray("Allowed");
		
		candidateFeedbackRepository.save(candidateFeedback);
	
		super.action(input, workFlow, action);
	}

}
