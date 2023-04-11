package com.knf.dev.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping("/findAll")
	public List<Hello> findAll(){
		return helloService.findAll();
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<String> findById(@PathVariable String id){
		return helloService.findById(id);
	}
	@GetMapping("/findByName/{name}")
	public ResponseEntity<String> findByName( @PathVariable String name){
		return helloService.findByName(name);
	}
	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody Hello hello){
		return helloService.save(hello);
		
	}
}
