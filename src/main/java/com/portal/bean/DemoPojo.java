package com.portal.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="DemoPojo")
public class DemoPojo {

	private String id;
	private String name;
}
