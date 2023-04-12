package com.portal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UsersExceptionHandler {

	// add an exception handler for products not found exception

	@ExceptionHandler
	public ResponseEntity<UsersErrorResponse> handleException(UserNotFoundException exc) {

		// create ProductserrorResponse
		UsersErrorResponse error = new UsersErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());

		// create response entity
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// add another exception handler to catch any exception

	@ExceptionHandler
	public ResponseEntity<UsersErrorResponse> handleException(Exception exc) {

		// create ProductserrorResponse
		UsersErrorResponse error = new UsersErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());

		// create responsibility
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}