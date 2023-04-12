package com.portal.validations;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

import com.portal.action.ActionValidation;
import com.portal.bean.WorkFlowBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkFlowInputValidator implements ConstraintValidator<ValidateWorkFlowInput, WorkFlowBean> {

	@Value("#{${portal.validations}}")
	Map<String, String> validationClassMap;

	@Override
	public boolean isValid(WorkFlowBean workFlowBean, ConstraintValidatorContext context) {
		try {
			if (validationClassMap == null || !validationClassMap.containsKey(workFlowBean.getAction())) {
				return true;
			}
			if (validationClassMap.containsKey(workFlowBean.getAction())) {
				String name = validationClassMap.get(workFlowBean.getAction());
				Class c = Class.forName(validationClassMap.get(workFlowBean.getAction()));
				ActionValidation validate = (ActionValidation) c.newInstance();
				return validate.Validate(workFlowBean.getValueMap());
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error("Error in reading class : "+e);
		}
		return false;
	}

}
