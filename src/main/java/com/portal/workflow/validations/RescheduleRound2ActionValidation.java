package com.portal.workflow.validations;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.portal.action.ActionConstants;
import com.portal.action.ActionValidation;

public class RescheduleRound2ActionValidation implements ActionValidation<Map<String, Object>> {

	@Override
	public boolean Validate(Map<String, Object> payLoad) {
		if(StringUtils.isEmpty((String) payLoad.get(ActionConstants.CANDIDATE_ID)) || StringUtils.isEmpty((String) payLoad.get("interviewerId")) || 
				StringUtils.isEmpty((String) payLoad.get("interviewDate")) || StringUtils.isEmpty((String) payLoad.get("interviewTime")) ||
				((Integer) payLoad.get("timeTakenForInterview")) == null || StringUtils.isEmpty((String) payLoad.get("criteriaGroup"))) {
			return false;
		}
		return true;
	}
}
