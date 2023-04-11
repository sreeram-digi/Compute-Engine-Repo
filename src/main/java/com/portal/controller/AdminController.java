package com.portal.controller;

import java.net.MalformedURLException;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwk.JwkException;
import com.portal.ApplicationConstants;
import com.portal.bean.LoginPayload;
import com.portal.bean.UserResponse;
import com.portal.bean.WorkFlowBean;
import com.portal.exception.UserNotFoundException;
import com.portal.service.AdminService;
import com.portal.validations.ValidateWorkFlowInput;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(maxAge = 3600)
@Validated
public class AdminController {
	
	private AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@Operation(summary = "This method is used to login")
	@PostMapping(value = "/login")
	public UserResponse login(@RequestBody @Valid LoginPayload loginPayload) throws UserNotFoundException {
		return adminService.login(loginPayload.getUserName(), loginPayload.getPassword());
	}

	@PostMapping(value = "/workflow")
	public void workflowAction(@RequestBody @Valid @ValidateWorkFlowInput  WorkFlowBean workFlowBean,@RequestHeader(value="token", required = false) String token) throws Exception {	
		workFlowBean.getValueMap().put(ApplicationConstants.JWT_TOKEN,token);
		adminService.callWorkFlow(workFlowBean,false, null);
	}
	
	@PostMapping(value = "/workflow/{cancel}")
	public void workflowCancelAction(@RequestBody WorkFlowBean workFlowBean, @PathVariable(name = "cancel") String cancel, @RequestHeader (value="token", required =false) String token) throws Exception {	
		workFlowBean.getValueMap().put(ApplicationConstants.JWT_TOKEN,token);
		adminService.callWorkFlow(workFlowBean,false, cancel);
	}

	@PostMapping(value = "/loginWithToken")
	public UserResponse loginWithToken(@RequestBody String token) throws MalformedURLException, JwkException, UserNotFoundException {
		return adminService.validateAndLogin(token);
	}
	
	
}