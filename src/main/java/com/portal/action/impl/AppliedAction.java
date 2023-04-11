package com.portal.action.impl;

import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.action.ActionConstants;
import com.portal.bean.CandidateFeedback;
import com.portal.repository.CandidateFeedbackRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AppliedAction extends BaseAction {

	@Autowired
	private CandidateFeedbackRepository candidateFeedbackRepository;

	
	@Override
	public void action(Map<String, Object> object, JSONObject workFlowNode, String action) throws Exception {
		Optional<CandidateFeedback> canFeedback = candidateFeedbackRepository
				.findById((String) object.get(ActionConstants.CANDIDATE_ID));
		if (canFeedback.isPresent()) {
			throw new Exception("User already exists");
		}
		String candidateId = (String) object.get(ActionConstants.CANDIDATE_ID);
		CandidateFeedback candidateFeedback = new CandidateFeedback();
		candidateFeedback.setId(candidateId);
		candidateFeedback.setStatus(action);
		candidateFeedbackRepository.save(candidateFeedback);
		
		super.action(object, workFlowNode, action);
	}

}
