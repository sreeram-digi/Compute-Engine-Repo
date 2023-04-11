package com.portal.service;

import java.util.List;

import javax.validation.Valid;

import com.portal.bean.Job;
import com.portal.exception.UserNotFoundException;

public interface JobService {

	Job saveJob(Job job);

	List<Job> getAllJob();

	Job getJobById(String id) throws UserNotFoundException;

	Job updateJob(Job job) throws UserNotFoundException;

	void deleteJobId(String id);
}
