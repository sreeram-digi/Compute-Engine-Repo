package com.portal.bean;

import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkFlowBean {
	
	@NotNull
	private String action;
	
	private Map<String, Object> valueMap;

}
