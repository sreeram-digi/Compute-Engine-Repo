package com.portal.bean;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "criteria")
public class Criteria {
	
	@Id
	@NotNull
	private String id; 
	
	@NotNull
	private String type;
	
}
