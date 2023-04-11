package com.portal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bean.Interviewer;
import com.portal.bean.Job;
import com.portal.exception.UserNotFoundException;
import com.portal.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600)
public class JobController {

	private JobService jobService;

	public JobController(JobService jobService) {
		this.jobService = jobService;
	}

	@Operation(summary = " This method is used to Createjob")
	@PostMapping("/job")
	public Job createJob(@Valid @RequestBody Job job) {
		log.debug("createJob() :creating the job:" + job.getId());
		return jobService.saveJob(job);
	}

	@Operation(summary = " This method is used to get all jobs")
	@GetMapping("/job")
	public List<Job> getAllJobs() {
		log.debug("getAllJobs() :display all the jobs:");
		return jobService.getAllJob();
	}

	@Operation(summary = " This method is used to get all jobs")
	@GetMapping("/job/{id}")
	public Job getJobById(@PathVariable(value = "id") String id) throws UserNotFoundException {
		return jobService.getJobById(id);
	}

	@Operation(summary = " This method is used to update job by id")
	@PutMapping("/job")
	public Job updateJob(@RequestBody Job job) throws UserNotFoundException {
		log.debug("updatJob():updating the job fields:" + job);
		return this.jobService.updateJob(job);
	}
	@Operation(summary = " This method is used to get all jobs")
	@DeleteMapping("/job/{id}")
	public void deleteJob(@PathVariable(value = "id") String id) {
		log.debug("deleteJob():deleteing the job by id " + id);
		jobService.deleteJobId(id);
	}
}