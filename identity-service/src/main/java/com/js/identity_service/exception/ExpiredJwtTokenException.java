package com.js.identity_service.exception;

public class ExpiredJwtTokenException extends Exception {

	public ExpiredJwtTokenException(String msg) {
		super(msg);
	}
}
