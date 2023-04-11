package com.portal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.bean.LoginPayload;
import com.portal.bean.UserResponse;
import com.portal.service.AdminService;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private AdminService adminService;

	@MockBean
	private AdminController adminController;

	private ObjectMapper objectMapper;

	private WebApplicationContext webApplicationContext;

	public AdminControllerTest(WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void login() throws Exception {
		when(adminController.login(any(LoginPayload.class))).thenReturn(new UserResponse());
		mockMvc.perform(post("/login").content("{\"userName\":\"raki\",\"password\":\"raki123\",\"type\": \"admin\"}")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	@Test
	public void workflowAction() {
	}
}