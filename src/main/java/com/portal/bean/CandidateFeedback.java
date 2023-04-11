package com.portal.bean;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "feed_back")
public class CandidateFeedback {

	@Id
	private String id;

	private List<CandidateHistory> candidateHistory;

	private String status;
	
	private String criteriaGroup;
	
	private String currentInterviewId;
	
	private String nextInterviewDate;

	private String nextInterviewTime;
	
	private Integer timeTakenForInterview;
	
	private Map<String, Object> feedBack;
	
	private String overAllFeedBack;
	
	private String eventId;
	
	private String interviewerName;

	@Transient
	private List<String> nextSteps;
	
}
