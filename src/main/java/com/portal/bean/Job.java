package com.portal.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
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
    private long numberOfPositionsAvaiable;

    @NotNull
    private String minimumAndMaximumExprience;

    @NotNull
    private String minimumAndMaximumSalary;

    @NotNull
    private List<Interviewer> recurtierAssigned;

    @NotNull
    private List<Interviewer> interviewerPanel;

    @NotNull
    private boolean openForVendors;

    @NotNull
    private boolean internal;

    @NotNull
    private String skillSet;
    
    @NotNull
    private String workType;
    
    @CreatedDate
    private LocalDateTime jobPostingDate;

    @NotNull
    private LocalDateTime startingDateOfHiring;
    
    @NotNull
    private LocalDateTime endingDateOfHiring;
    
	
}
