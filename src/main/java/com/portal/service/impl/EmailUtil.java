package com.portal.service.impl;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.ApplicationConfigurations;
import com.portal.ApplicationConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Setter
@Slf4j
public class EmailUtil {

	@Autowired
	private ApplicationConfigurations applicationConfigurations;
	
	public String constructEmailSubject(String emailType, Object... dynamicValues)
	{
		String emailSubject = "";
		
		if(ApplicationConstants.ASSIGN_SELECTOR.equalsIgnoreCase(emailType))
			emailSubject = applicationConfigurations.getAssignSelectorSubject();
		
		else if(ApplicationConstants.SHORTLIST_CANDIDATE.equalsIgnoreCase(emailType))
			emailSubject = applicationConfigurations.getShortListCandidateSubject();
		
		else if(ApplicationConstants.ASSIGN_INTERVIEWER.equalsIgnoreCase(emailType))
			emailSubject = applicationConfigurations.getAssignInterviewerSubject();
		
		else if(ApplicationConstants.ATTEND_INTERVIEW.equalsIgnoreCase(emailType))
			emailSubject = applicationConfigurations.getAttendInterviewSubject();
		
		return formatMessage(emailSubject, dynamicValues);
	}
	
	public String constructEmailBody(String emailType, Object... dynamicValues)
	{
		String emailBody = "";
		
		if(ApplicationConstants.ASSIGN_SELECTOR.equalsIgnoreCase(emailType))
			emailBody = applicationConfigurations.getAssignSelector();
		
		else if(ApplicationConstants.SHORTLIST_CANDIDATE.equalsIgnoreCase(emailType))
			emailBody = applicationConfigurations.getShortListCandidate();
		
		else if(ApplicationConstants.ASSIGN_INTERVIEWER.equalsIgnoreCase(emailType))
			emailBody = applicationConfigurations.getAssignInterviewer();
		
		else if(ApplicationConstants.ATTEND_INTERVIEW.equalsIgnoreCase(emailType))
			emailBody = applicationConfigurations.getAttendInterview();
		
		return formatMessage(emailBody, dynamicValues);
	}
	
	private String formatMessage(String message, Object... args)
	{
		return String.format(message, args);
	}
	
	public static boolean isValidEmail(String email)
	{
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
				"[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
				"A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}
}
