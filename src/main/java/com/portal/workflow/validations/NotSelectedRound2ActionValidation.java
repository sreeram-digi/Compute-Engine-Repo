package com.portal.workflow.validations;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.portal.action.ActionValidation;

public class NotSelectedRound2ActionValidation implements ActionValidation<Map<String, Object>>{

	@Override
	public boolean Validate(Map<String, Object> payLoad) {
		if(StringUtils.isEmpty((String) payLoad.get("candidateId")) || StringUtils.isEmpty((String) payLoad.get("interviewerId")) 
				|| payLoad.get("feedBack") == null || StringUtils.isEmpty((String) payLoad.get("overAllFeedBack"))){
			return false;
		}
		
		return true;
	}

}
