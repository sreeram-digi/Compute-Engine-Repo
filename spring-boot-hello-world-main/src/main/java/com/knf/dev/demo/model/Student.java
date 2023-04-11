package com.knf.dev.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Student")
public class Student {

	@Id
	private String id;
	private String name;
	private int age;
	
}
