package com.portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.portal.bean.DemoPojo;
import com.portal.service.DemoService;

@RestController
public class DemoController {

	DemoService demoService;

	public DemoController(DemoService demoService) {
		super();
		this.demoService = demoService;
	}
	
	@GetMapping("/getDummyData/{id}")
	public DemoPojo getDataById(@PathVariable String id) {
		return demoService.findById(id);
	}
}
