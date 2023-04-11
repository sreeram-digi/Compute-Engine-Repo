package com.portal.validations;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.portal.bean.Candidate;
import com.portal.bean.WorkFlowBean;
import com.portal.repository.CandidateRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailPhoneNumberDbValidator implements ConstraintValidator<ValidateEmailPhoneNumberDb, Candidate> {

	@Autowired
	private CandidateRepository canRepo;
	
	@Override
	public boolean isValid(Candidate value, ConstraintValidatorContext context) {
		List<Candidate> cans = canRepo.findByEmailOrPhoneNumber(value.getEmail(), value.getPhoneNumber());
		if(cans != null && cans.size()>0) {
			return false;
		}
		return true;
	}

}
