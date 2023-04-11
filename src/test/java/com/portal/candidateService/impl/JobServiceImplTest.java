package com.portal.candidateService.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portal.bean.Interviewer;
import com.portal.bean.Job;
import com.portal.repository.JobRepository;
import com.portal.service.JobService;
import com.portal.service.impl.JobServiceImpl;

@ExtendWith(MockitoExtension.class)
public class JobServiceImplTest {

	private JobService jobService;

	@Mock
	private JobRepository jobRepository;

	private Job job1;
	private Job job2;
	List<Job> jobList;

	@BeforeEach
	public void setUp() {
		jobService = new JobServiceImpl(jobRepository);
		jobList = new ArrayList<>();
		job1 = new Job();
		job2 = new Job();
		jobList.add(job1);
		jobList.add(job2);
	}

	@AfterEach
	public void tearDown() {
		job1 = job2 = null;
		jobList = null;
	}

	@Test
	public void saveJob() throws Exception {
		when(jobRepository.save(any(Job.class))).thenReturn(new Job());
		Job j = new Job();
		j.setJobTitle("java developer");
		j.setJobDescription("java springboot");
		j.setPositions(4);
		assertThat(jobService.saveJob(j)).isNotNull();
	}

	@Test
	public void getAllInterviewers() throws Exception {
		jobRepository.save(job1);
		when(jobService.getAllJob()).thenReturn(jobList);
		List<Job> jobList1 = jobService.getAllJob();
		assertEquals(jobList1, jobList);

	}

	@Test
	public void updateJob() throws Exception {
		Optional<Job> inte = Optional.of(new Job());
		when(jobRepository.findById(anyString())).thenReturn(inte);
		Job job = new Job();
		job.setId("23456");
		assertThat(jobService.updateJob(job)).isNotNull();
	}

	@Test
	public void deleteJobId() throws Exception {
		Job job = new Job();
		jobRepository.save(job);
		jobRepository.deleteById(job.getId());
		Optional<Job> optional = jobRepository.findById(job.getId());
		assertEquals(Optional.empty(), optional);
	}
}
