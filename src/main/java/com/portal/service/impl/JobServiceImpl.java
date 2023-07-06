package com.portal.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.portal.bean.Candidate;
import com.portal.bean.Job;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.CandidateRepository;
import com.portal.repository.JobRepository;
import com.portal.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

	private JobRepository jobRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;

	public JobServiceImpl(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Value("${resume.paths}")
	private String path;

	@Override
	public Job saveJob(Job job) {
		log.debug("saveJob(): saving the Job : " + job);

		List<String> jobSkillSetArray =Arrays.asList(job.getSkillSet().split(","));

		File fileObject = new File(path);
		List<File> fileListPresent=Arrays.asList(fileObject.listFiles());

		List<String> fileNames=	fileListPresent.stream().map(File::getName).collect(Collectors.toList());
		
		for(String skillsObject:jobSkillSetArray) {

			if(!fileNames.contains(skillsObject)) {
				File newFile = new File(path+"/"+skillsObject);
				if(!newFile.exists())
					newFile.mkdir();
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

	
	/**
	 * @author Revathi Muddani
	 * @return List<File>
	 * 
	 * {@summary: This method is used to return the count and list of files(resumes) according to JobId }
	 * We can Count no.of resumes by using this method
	 */
	
	@Override
	public List<File> getallresumesbyJobId(String jobId) {

		int count = 0;
		Job job = jobRepository.findById(jobId).get();
		String[] skillSet = job.getSkillSet().split(",");
		List<String> folderNames=	Arrays.asList(new File(path).listFiles()).stream().map(File::getName).filter(name->name.equals(skillSet[0])).toList();
		List<File> filesList = new ArrayList<>();
		List<File> tempFile =  Arrays.asList(new File(path+"/"+folderNames.get(0)).listFiles());
		
		for(Candidate candidate : candidateRepository.findByJobId(jobId)) {

			for(File file : tempFile) {

				if(file.getName().contains(candidate.getId())) {
					count++;
					filesList.add(file);
					break;
				}
			}
		}
		System.out.println(count);
		return filesList;
	}
	
	
}
