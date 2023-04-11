package com.portal.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginTypeValidator implements ConstraintValidator<LoginType, String> {
	String[] comparision;

	@Override
	public void initialize(LoginType constraintAnnotation) {
		this.comparision = constraintAnnotation.values();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value != null) {
			for (String loginType : comparision) {
				if (loginType.equalsIgnoreCase(value)) {
					return true;
				}
			}
		}
		return false;
	}
}