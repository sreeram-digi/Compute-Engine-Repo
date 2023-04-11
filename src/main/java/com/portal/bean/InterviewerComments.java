package com.portal.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewerComments {
	
	private String interviwerId;
	
	private String candidateId;
	
	private String feedBack;
	
	private String roundStatus;
	
	private Status status;

}
