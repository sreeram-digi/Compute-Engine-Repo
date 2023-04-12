package com.portal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.portal.bean.Candidate;
import com.portal.bean.UpdateCandidatePayload;
import com.portal.service.AdminService;
import com.portal.service.CandidateService;

@WebMvcTest(CandidateController.class)
public class CandidateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CandidateService candidateService;

	@MockBean
	private AdminService adminService;

	@Test
	public void createCandidate() throws Exception {

		when(candidateService.createCandidate(any(Candidate.class))).thenReturn(new Candidate());
		mockMvc.perform(post("/candidate").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "	\"firstName\":\"test\",\r\n" + "	\"lastName\":\"test\",\r\n"
						+ "	\"phoneNumber\":\"999999\",\r\n" + "	\"email\":\"test@digi.com\",\r\n"
						+ "	\"skills\":\"java\",\r\n" + "	\"jobTitle\":\"Developer\",\r\n"
						+ "	\"experience\":\"5\",\r\n" + "	\"relavantExperience\":\"6\"\r\n" + "}"))
				.andExpect(status().is2xxSuccessful());

	}

	@Test
	public void getAllCandiddates() throws Exception {
		when(candidateService.getAllCandidates(anyString())).thenReturn((List<Candidate>) new ArrayList<Candidate>());
		mockMvc.perform(get("/candidate").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void getCandidateById() throws Exception {
		when(candidateService.getCandidateById(anyString())).thenReturn(new Candidate());
		mockMvc.perform(get("/candidate/8003").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void updateCandidateById() throws Exception {
		when(candidateService.updateCandidateById(any(UpdateCandidatePayload.class))).thenReturn(new Candidate());
		mockMvc.perform(put("/candidate").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "	\"firstName\":\"test\",\r\n" + "	\"lastName\":\"test\",\r\n"
						+ "	\"phoneNumber\":\"999999\",\r\n" + "	\"email\":\"test@digi.com\",\r\n"
						+ "	\"skills\":\"java\",\r\n" + "	\"jobTitle\":\"Developer\",\r\n"
						+ "	\"experience\":\"5\",\r\n" + "	\"relavantExperience\":\"5\"\r\n" + "}"))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void deleteCandidateById() throws Exception {
		mockMvc.perform(delete("/candidate/2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void getListOfCandidatesOnStatus() throws Exception {
		when(candidateService.getAllCandidatesBasedOnstatus(any(List.class))).thenReturn(new ArrayList<Candidate>());
		mockMvc.perform(get("/candidate/status").contentType(MediaType.APPLICATION_JSON).content("[\"Applied\"]"))
				.andExpect(status().isOk());
	}
}