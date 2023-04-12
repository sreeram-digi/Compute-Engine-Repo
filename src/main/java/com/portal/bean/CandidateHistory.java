package com.portal.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateHistory {

  private String interviwerId;
	
	private Map<String, Object> feedBack;
	
	private String overAllFeedBack;
	
	private String roundStatus;
	
	private String interviewDate;
	
	private String interviewTime;
	
	private String interviewerName;
	
	private LocalDateTime lastModifiedDate;
}
