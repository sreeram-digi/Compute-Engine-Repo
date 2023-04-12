package com.portal.workflow.validations;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.portal.action.ActionValidation;

public class SelectedRound1ActionValidation implements ActionValidation<Map<String, Object>>{

	@Override
	public boolean Validate(Map<String, Object> payLoad) {
		if(StringUtils.isEmpty((String) payLoad.get("candidateId")) ||  payLoad.get("feedBack") == null
				|| StringUtils.isEmpty((String) payLoad.get("overAllFeedBack"))){
			return false;
		}
		
		return true;
	}

}
