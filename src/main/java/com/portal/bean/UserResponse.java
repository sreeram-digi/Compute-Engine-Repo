package com.portal.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
	
	private String id;
	private String userName;
	private Boolean status;
	private String token;
	private boolean selector;
	private boolean hr;
	private boolean admin;
	private boolean externalUser;
}
