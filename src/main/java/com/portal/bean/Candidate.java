package com.portal.bean;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "candidate")
public class Candidate {

	@Id
	private String id;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@NotNull
	private String phoneNumber;

	@NotNull
	@Email
	private String email;

	@NotNull
	private String skills;

	@NotNull
	private String jobDescription;

	@NotNull
	private String jobTitle;

	@NotNull
	private double experience;

	@NotNull
	private double relavantExperience;
	
	@NotNull
	private double currentCtc;

	@NotNull
	private double expectedCtc;

	@NotNull
	private String noticePeriod;
	
	
	private String currentOrganisation;

	private String resume;

	private String selectorId;
	
	@Transient
	private List<String> pointOfContactIds;
	
	private List<Interviewer> pointOfContact;

	private LocalDateTime lastModifiedDate;
	
	@CreatedDate
	private LocalDateTime uploadedDate;
	
	public enum employmentType{
		Permanent,Contractor;
	}

	@DBRef
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Interviewer upLoader;
	
	private String upLoadedBy;
	
	@DBRef
	private CandidateFeedback candidateFeedback;

	@Transient
	private String uploadedByName;
	
	@Transient
	private String uploadedByEmail;
	
	@NotNull
	private String jobId;
	
	public Interviewer getUpLoader()
	{
		if (upLoader != null)
			return upLoader;
		
		return null;
			
	}
	public String getUploadedByEmail() 
	{
		if(upLoader!=null)
			return upLoader.getInterviewerEmail();
		return null;
	}
	
	public String getUploadedByName()
	{
		if(upLoader!=null)
			return upLoader.getInterviewerName();
		
		return null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
