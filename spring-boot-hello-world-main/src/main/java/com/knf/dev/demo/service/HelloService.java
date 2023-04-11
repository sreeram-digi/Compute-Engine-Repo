package com.knf.dev.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.knf.dev.demo.entity.Hello;

public interface HelloService {

	List<Hello> findAll();

	ResponseEntity<String> findById(String id);

	ResponseEntity<String> findByName(String name);

}
