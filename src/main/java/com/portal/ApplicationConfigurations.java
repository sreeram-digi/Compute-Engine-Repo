package com.portal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class ApplicationConfigurations {

	@Value("${portal.configuration.secretKey}")
	private String secretKey;
	
    @Value("${portal.configuration.fromemail}")
	private String fromemail;
	
	@Value("${portal.configuration.emailPassword}")
	private String emailPassword;
	
	@Value("${portal.configuration.saveEmailToSentItems}")
	private boolean saveToSentItems;
	
	@Value("${portal.configuration.adminNotificationEmail}")
	private String adminNotificationEmail;
	
	@Value("${portal.configuration.portalURL}")
	private String portalURL;
	
	@Value("${portal.configuration.emailBody.assignSelector}")
	private String assignSelector;
	
	@Value("${portal.configuration.emailBody.shortListCandidate}")
	private String shortListCandidate;
	
	@Value("${portal.configuration.emailBody.assignInterviewer}")
	private String assignInterviewer;
	
	@Value("${portal.configuration.emailBody.attendInterview}")
	private String attendInterview;
	
	@Value("${portal.configuration.emailSubject.assignSelector}")
	private String assignSelectorSubject;
	
	@Value("${portal.configuration.emailSubject.shortListCandidate}")
	private String shortListCandidateSubject;
	
	@Value("${portal.configuration.emailSubject.assignInterviewer}")
	private String assignInterviewerSubject;
	
	@Value("${portal.configuration.emailSubject.attendInterview}")
	private String attendInterviewSubject;
	
}
