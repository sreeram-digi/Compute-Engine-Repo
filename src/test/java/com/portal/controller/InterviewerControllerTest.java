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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.portal.bean.Interviewer;
import com.portal.service.InterviewerService;

@WebMvcTest(InterviewerController.class)
public class InterviewerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private InterviewerService interviewerService;

	@Test
	public void createInterviewer() throws Exception {
		when(interviewerService.saveInterviewer(any(Interviewer.class))).thenReturn(new Interviewer());
		mockMvc.perform(post("/interviewer").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "    \"interviewerName\":\"Ramesh\",\r\n"
						+ "    \"interviewerEmail\":\"Rameshk@gmail.com\",\r\n"
						+ "    \"interviewerPassword\":\"Passwo990123\"\r\n" + "}"))
				.andExpect(status().is2xxSuccessful());

	}

	@Test
	public void updateInterviewer() throws Exception {
		when(interviewerService.updateInterviewer(any(Interviewer.class))).thenReturn(new Interviewer());
		mockMvc.perform(put("/interviewer").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "    \"interviewerName\":\"Ramesh\",\r\n"
						+ "    \"interviewerEmail\":\"Rameshk@gmail.com\",\r\n"
						+ "    \"interviewerPassword\":\"Passwo990123\"\r\n" + "}"))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void getInterviewerById() throws Exception {

		when(interviewerService.getInterviewerById(anyString())).thenReturn(new Interviewer());
		mockMvc.perform(get("/interviewer/156789ju").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	@Test
	public void getAllInterviewers() throws Exception {

		when(interviewerService.getAllInterviewers(1, 1)).thenReturn((Page<Interviewer>) new ArrayList<Interviewer>());
		mockMvc.perform(get("/interviewer").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void DeleteInterviewerById() throws Exception {
		mockMvc.perform(delete("/interviewer/156789ju").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getAllSelectors() throws Exception {
		when(interviewerService.getAllSelectors()).thenReturn(new ArrayList<Interviewer>());
		mockMvc.perform(get("/interviewer/selectors").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}
}
