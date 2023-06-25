package com.portal.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "interviewer")
public class Interviewer {
	
	@Id
	private String id;
	
	private String interviewerName;
	
	private String interviewerEmail;
	
	private String interviewerPassword;
	
	private boolean selector;
	
	private boolean hr;
	
	private boolean admin;
	
	private boolean externalUser;
	
	private LocalDateTime dateOfAssignedPosition;
	
	public String getInterviewerEmail() {
		return interviewerEmail.toLowerCase();
	}
}
