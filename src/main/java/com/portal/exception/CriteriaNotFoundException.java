package com.portal.exception;

public class CriteriaNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public CriteriaNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	public CriteriaNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CriteriaNotFoundException(Throwable cause) {
		super(cause);
	}

	protected CriteriaNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
