package com.portal.bean;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class UpdateCandidatePayload {
	
	@NotNull
	private String id;

	private String firstName;
	
	private String lastName;
	
	private String phoneNumber;
	
	@Email
	private String email;
	
	private String skills;
	
	private String jobDescription;
	
	private String jobTitle;
	
	private double experience;
	
	private double relavantExperience;
	
	private String selectorId;
	
	private String upLoadedBy;
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
