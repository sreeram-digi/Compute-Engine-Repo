package com.knf.dev.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knf.dev.demo.entity.Hello;
import com.knf.dev.demo.service.HelloService;

@RestController
public class HelloController {

	private HelloService helloService;

	public HelloController(HelloService helloService) {
		super();
		this.helloService = helloService;
	}

	@GetMapping("/hello")
	public String getMessage() {
		return "Hello World!";
	}

	@GetMapping("/getInfo")
	public String getInfo() {
		return "Hi All";
	}

	@GetMapping("/callName")
	public String callName() {
		return "Naga Sreeram";
	}
	
	@PostMapping("/findAll")
	public List<Hello> findAll(){
		return helloService.findAll();
	}

}
