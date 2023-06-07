package com.portal.response;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class JobResponse {

	private String jobDescription;

	private String location;
	
	private String jobTitle;

	private String startingDateOfHiring;;

	private String endingDateOfHiring;;

	private long positions;
	
	private String exprience;
	
	private String salary;
}
