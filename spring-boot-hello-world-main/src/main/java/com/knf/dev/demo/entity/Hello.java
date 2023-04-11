package com.knf.dev.demo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "Hello")
@Data
public class Hello {
	
	private String id;
	private String name;
	private String phoneNumber;
	private String password;

}
