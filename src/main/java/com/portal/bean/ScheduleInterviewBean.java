package com.portal.bean;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleInterviewBean {
	
	private String interviwerId;
	
	private String candidateId;
	
	private LocalDate interviewDate;
	
	private LocalTime interviewStartTime;
	
	private LocalTime interviewEndTime;
}
