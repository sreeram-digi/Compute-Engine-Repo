package com.portal.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portal.bean.Job;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.JobRepository;
import com.portal.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

	private JobRepository jobRepository;

	public JobServiceImpl(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Override
	public Job saveJob(Job job) {
		log.debug("saveJob(): saving the Job : " + job);
		return this.jobRepository.save(job);
	}

	@Override
	public List<Job> getAllJob() {
		log.debug("getAllJobs(): getting all the jobss : ");
		return jobRepository.findAll();
	}

	@Override
	public Job getJobById(String id) throws UserNotFoundException {
		log.debug("getJobById(): getting job with id: " + id);
		return jobRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Job with Id " + id + " not found"));
	}

	@Override
	public Job updateJob(Job job) throws UserNotFoundException {
		log.debug("updateJob(): updating the job : " + job);
		jobRepository.findById(job.getId())
				.orElseThrow(() -> new UserNotFoundException("job with Id " + job.getId() + " not found"));
		this.jobRepository.save(job);
		return job;
	}

	@Override
	public void deleteJobId(String id) {
		log.debug("deleteJobId(): deleting job with id: " + id);
		jobRepository.deleteById(id);
		log.debug("deleteJobId():deleted the job by id:" + id);

	}
}
