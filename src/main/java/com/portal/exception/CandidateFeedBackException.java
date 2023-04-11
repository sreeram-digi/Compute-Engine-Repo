package com.portal.exception;

public class CandidateFeedBackException extends Exception{

	private static final long serialVersionUID = 1L;
		
	public CandidateFeedBackException(String errorMessage) {
		super(errorMessage);
	}

	public CandidateFeedBackException(String message, Throwable cause) {
		super(message, cause);
	}

	public CandidateFeedBackException(Throwable cause) {
		super(cause);
	}

	protected CandidateFeedBackException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
