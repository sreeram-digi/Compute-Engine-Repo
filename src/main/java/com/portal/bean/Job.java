package com.portal.bean;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "job")
public class Job {

	@Id
	private String id;

	@NotNull
	private String jobDescription;

	@NotNull
	private String location;
	
	@NotNull
	private String jobTitle;

	@NotNull
	private String startDate;

	@NotNull
	private String endDate;

	@NotNull
	private long positions;

}
