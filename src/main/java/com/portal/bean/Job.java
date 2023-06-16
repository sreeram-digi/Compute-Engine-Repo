package com.portal.bean;

import java.util.List;

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
	private String applyingPostions;
	
	@NotNull
    private long numberOfPositionsAvaiable;

    @NotNull
    private String minimumAndMaximumExprience;

    @NotNull
    private String minimumAndMaximumSalary;

    @NotNull
    private String recurtierAssigned;

    @NotNull
    private List<Interviewer> interviewerPanel;

    @NotNull
    private boolean openForVendors;

    @NotNull
    private boolean isInternal;

    @NotNull
    private String skillSet;
    
    @NotNull
    private String workType;
	
}
