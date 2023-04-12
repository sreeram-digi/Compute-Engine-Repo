package com.portal.bean;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;

@Getter
@Setter
@Document(collection="criteriaGroup")
public class CriteriaGroup {
	
	@Id
	@NotNull
	private String id;
	
	@NotNull
	private List<String> criteriaIds;
	
	@DBRef
	private List<Criteria> criterias;
}
