package com.portal.candidateService.impl;

import com.portal.bean.Interviewer;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.InterviewerRepository;
import com.portal.service.AdminService;
import com.portal.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

	private AdminService adminService;


	@Mock
	private InterviewerRepository interviewerRepo;



	@BeforeEach
	public void setUp() {
		adminService = new AdminServiceImpl(interviewerRepo);
	}


	@Test
	public void loginWithInterviewer() throws UserNotFoundException {
		Interviewer interviewer = new Interviewer();
		interviewer.setInterviewerName("interviewer1");
		interviewer.setInterviewerPassword("test@123");
		Optional<Interviewer> optionalInterviewer = Optional.of(interviewer);
		when(interviewerRepo.findByInterviewerEmailAndInterviewerPassword(anyString(), anyString()))
				.thenReturn(optionalInterviewer);
		assertThat(adminService.login("interviewer1", "test@123")).isNotNull();
	}

}