package com.portal.candidateService.impl;

import com.portal.bean.Interviewer;
import com.portal.repository.InterviewerRepository;
import com.portal.service.InterviewerService;
import com.portal.service.impl.InterviewerServiceimpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewerServiceImplTest {

	private InterviewerService interviewerService;

	@Mock
	private InterviewerRepository interviewerRepository;

	private Interviewer interviewer1;
	private Interviewer interviewer2;
	List<Interviewer> interviewerList;
	@MockBean
	Page<Interviewer> interviewerPage;

	@BeforeEach
	public void setUp() {
		interviewerService = new InterviewerServiceimpl(interviewerRepository);
		interviewerList = new ArrayList<>();
		interviewer1 = new Interviewer("13f7yyh" ,"Ganeshk", "GaneshK@gmail.com", "password7864", true, false, false, false);
		interviewer2 = new Interviewer("115f6ll", "Abhishek", "Abhishek1@gmail.com", "password98564", false, false, false,false);
		interviewerList.add(interviewer1);
		interviewerList.add(interviewer2);
	}

	@AfterEach
	public void tearDown() {
		interviewer1 = interviewer2 = null;
		interviewerList = null;
	}

	@Test
	public void saveInterviewer() throws Exception {
		when(interviewerRepository.save(any(Interviewer.class))).thenReturn(new Interviewer());
		Interviewer i = new Interviewer();
		i.setInterviewerEmail("GaneshK@gmail.com");
		//i.setInterviewerDesignation("Manager");
		i.setInterviewerName("Ganeshk");
		i.setInterviewerPassword("password7864");
		assertThat(interviewerService.saveInterviewer(i)).isNotNull();
	}

	@Test
	public void updateInterviewer() throws Exception {
		Optional<Interviewer> inte = Optional.of(new Interviewer());
		when(interviewerRepository.findById(anyString())).thenReturn(inte);
		Interviewer interviewer = new Interviewer();
		interviewer.setId("56768");
		assertThat(interviewerService.updateInterviewer(interviewer)).isNotNull();
	}

	@Test
	public void getAllInterviewers() throws Exception {
		interviewerRepository.save(interviewer1);
		
		when(interviewerService.getAllInterviewers(1, 1)).thenReturn(interviewerPage);
		Page<Interviewer> interviewerList1 = interviewerService.getAllInterviewers(1, 1);
		assertEquals(interviewerList1, interviewerPage);
		
	}

	@Test
	public void givenIdDeleteTheInterviewer() throws Exception {
		Interviewer interviewer = new Interviewer("14gytr557", "Ganeshk", "GaneshK@gmail.com",
				"password7864", true, false, false,false);
		interviewerRepository.save(interviewer);
		interviewerRepository.deleteById(interviewer.getId());
		Optional<Interviewer> optional = interviewerRepository.findById(interviewer.getId());
		assertEquals(Optional.empty(), optional);
	}

	@Test
	public void getAllSelectors() throws Exception{
		interviewerRepository.save(interviewer1);
		when(interviewerRepository.findBySelector(true)).thenReturn(interviewerList);
		List<Interviewer> interviewerList1 = interviewerService.getAllSelectors();
		assertEquals(interviewerList1, interviewerList);
	}@Test
	public void getAllAdmins() throws Exception{
		interviewerRepository.save(interviewer1);
		when(interviewerRepository.findByAdmin(true)).thenReturn(interviewerList);
		List<Interviewer> interviewerList1 = interviewerService.getAllAdmins();
		assertEquals(interviewerList1, interviewerList);
	}
	@Test
	public void getAllHrs() throws Exception{
		interviewerRepository.save(interviewer1);
		when(interviewerRepository.findByHr(true)).thenReturn(interviewerList);
		List<Interviewer> interviewerList1 = interviewerService.getAllhrs();
		assertEquals(interviewerList1, interviewerList);
	}
}