package com.portal.candidateService.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import com.portal.bean.Candidate;
import com.portal.bean.CandidateFeedback;
import com.portal.bean.UpdateCandidatePayload;
import com.portal.exception.UserNotFoundException;
import com.portal.repository.CandidateFeedbackRepository;
import com.portal.repository.CandidateRepository;
import com.portal.repository.InterviewerRepository;
import com.portal.service.AdminService;
import com.portal.service.CandidateService;
import com.portal.service.impl.CandidateServiceImpl;
import com.portal.service.impl.EmailUtil;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceImplTest {

	private CandidateService candidateService;

	@Mock
	private CandidateRepository candidateRepository;

	@Mock
	private CandidateFeedbackRepository candidateFeedbackRepository;
	
	@Mock
	private AdminService adminService;
	
	@Mock
	private InterviewerRepository interviewerRepository;

	@MockBean
	private Candidate candidate1;
	@MockBean
	private Candidate candidate2;
	List<Candidate> candidateList;
	private CandidateFeedback candidateFeedback1;
	List<CandidateFeedback> candidateFeedbackList;
	@MockBean
	Page<Candidate> candidatePage;

	@Mock
	private EmailUtil emailUtil;
	
	@BeforeEach
	public void setUp() {
		candidateService = new CandidateServiceImpl(candidateRepository, candidateFeedbackRepository, adminService,interviewerRepository,emailUtil);
		candidateList = new ArrayList<>();
		candidateList.add(candidate1);
		candidateList.add(candidate2);
		candidateFeedbackList = new ArrayList<>();
		candidateFeedback1 = new CandidateFeedback();
		candidateFeedbackList.add(candidateFeedback1);
	}

	@Test
	public void saveCandidate() throws Exception {
		when(candidateRepository.save(any(Candidate.class))).thenReturn(new Candidate());
		Candidate c = new Candidate();
		c.setEmail("test@tesr.com");
		assertThat(candidateService.createCandidate(c)).isNotNull();
	}

	@Test
	public void getAllCandidatesWithExternalUser() throws Exception {
		when(candidateRepository.findAllUploadedBy(anyString())).thenReturn(candidateList);
		assertThat(candidateService.getAllCandidates("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJESUdJU1BSSU5UIiwic3ViIjoiYWNjZXNzIiwidXNlck5hbWUiOiJhZG1pbkBhZG1pbi5jb20i"
				+ "LCJ1c2VySWQiOiJBZG1pbiIsInR5cGUiOiJleHRlcm5hbFVzZXIiLCJhY2Nlc3MiOlsiZXh0ZXJuYWxVc2VyIl0sImlhdCI6MTY1Nzg5MjYwMiwiZXhwIjoxNjU3ODkyNjAyfQ.86Fq9kJWXZaLVby1gIqpsXnEhkF4Pvu5-6ssXoC0KUU")).isNotNull();
		
	}
	
	@Test
	public void getAllCandidates() throws Exception {
		when(candidateRepository.findAll()).thenReturn(candidateList);
		assertThat(candidateService.getAllCandidates("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJESUdJU1BSSU5UIiwic3ViIjoiYWNjZXNzIiwidXNlck5hbWUiOiJhZG1"
				+ "pbkBhZG1pbi5jb20iLCJ1c2VySWQiOiJBZG1pbiIsInR5cGUiOiJpbnRlcm5hbFVzZXIiLCJhY2Nlc3MiOlsiQWRtaW4iLCJTZWxlY3RvciIsIkhyIl0sImlhdCI6MTY1Nzg5Njc5NCwiZXhwIjoxNjU3ODk2Nzk0fQ._etQdagb_Ay8igC29XDE5tJh_UGDLSnk79a630oIwQU")).isNotNull();
		
	}

	@Test
	public void updateCandidateById() throws Exception {
		Optional<Candidate> can = Optional.of(new Candidate());
		when(candidateRepository.findById(anyString())).thenReturn(can);
		UpdateCandidatePayload candidate = new UpdateCandidatePayload();
		candidate.setId("123");
		assertThat(candidateService.updateCandidateById(candidate)).isNotNull();
	}

	@Test
	public void updateCandidateByIdWithNull() throws Exception {
		Optional<Candidate> can = Optional.ofNullable(null);
		when(candidateRepository.findById(anyString())).thenReturn(can);
		UpdateCandidatePayload candidate = new UpdateCandidatePayload();
		candidate.setId("123");
		assertThrows(UserNotFoundException.class, () -> {
			candidateService.updateCandidateById(candidate);
		});
	}

	@Test
	public void getCandidateById() throws Exception {
		Optional<Candidate> can = Optional.of(new Candidate());
		when(candidateRepository.findById(anyString())).thenReturn(can);
		when(candidateFeedbackRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThat(candidateService.getCandidateById("223")).isNotNull();

	}

	@Test
	public void getCandidateByIdWithNull() throws Exception {
		Optional<Candidate> can = Optional.ofNullable(null);
		when(candidateRepository.findById(anyString())).thenReturn(can);
		assertThrows(UserNotFoundException.class, () -> {
			candidateService.getCandidateById("000000");
		});
	}

	@Test
	public void deleteCandidateById() throws Exception {
		doNothing().when(candidateRepository).deleteById(anyString());
		candidateService.deleteCandidateById("123");
	}

	@Test
	public void findByEmail() throws Exception {
		when(candidateRepository.findByEmail(anyString())).thenReturn(candidate1);
		candidateService.findByEmail("andy@gmail.com");
	}

	@Test
	public void findByPhoneNumber() throws Exception {
		when(candidateRepository.findByphoneNumber(anyString())).thenReturn(candidate1);
		candidateService.findByphoneNumber("8747474637");
	}
	@Test
	public void getAllCandidatesBasedOnstatus() throws Exception {
		List<String> canFeedback = new ArrayList<>();
		canFeedback.add("Applied");
		List<CandidateFeedback> feedBacks = new ArrayList<>();
		CandidateFeedback candidateFeedBack = new CandidateFeedback();
		candidateFeedBack.setId("1234567890");
		feedBacks.add(candidateFeedBack);
		when(candidateFeedbackRepository.findAllBystatus(any(List.class))).thenReturn(feedBacks);
		when(candidateRepository.findAllById(any(List.class))).thenReturn(candidateList);
		assertThat(candidateService.getAllCandidatesBasedOnstatus(canFeedback)).isNotNull();

	}
	@Test
	public void getAllCandidatesExcel() {
		when(candidateRepository.findAll()).thenReturn(candidateList);
		assertThat(candidateService.getAllCandidatesExcel()).isNotNull();
		
	}
}
