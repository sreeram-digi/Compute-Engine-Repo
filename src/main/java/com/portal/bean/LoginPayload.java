package com.portal.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginPayload {
	
	@NotNull
	private String userName;
	@NotNull
	private String password;

}
