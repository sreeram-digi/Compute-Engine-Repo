package com.portal.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${resume.paths}")
	private String path;

	@Override
	public Job saveJob(Job job) {
		log.debug("saveJob(): saving the Job : " + job);

		List<String> jobSkillSetArray =Arrays.asList(job.getSkillSet().split(","));

		String pathOfFiles=path;

		File fileObject = new File(pathOfFiles);
		List<File> fileListPresent=Arrays.asList(fileObject.listFiles());

		List<String> fileNames=	fileListPresent.stream().map(File::getName).collect(Collectors.toList());

		for(String skillsObject:jobSkillSetArray) {

			if(!fileNames.contains(skillsObject)) {
				File newFile = new File(path+"/"+skillsObject);
				if(!newFile.exists())
					newFile.mkdir();
				System.out.println("folder created");
			}
		}
		
		job.setJobPostingDate(LocalDateTime.now());
		
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
