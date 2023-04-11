package com.portal.exception;

public class CandidatePresentException  extends Exception{

	private static final long serialVersionUID = 1L;

	public CandidatePresentException(String errorMessage) {
		super(errorMessage);
	}

	public CandidatePresentException(String message, Throwable cause) {
		super(message, cause);
	}

	public CandidatePresentException(Throwable cause) {
		super(cause);
	}

	protected CandidatePresentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
