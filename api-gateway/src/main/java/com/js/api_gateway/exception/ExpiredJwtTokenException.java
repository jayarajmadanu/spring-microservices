package com.js.api_gateway.exception;

public class ExpiredJwtTokenException extends Exception {

	public ExpiredJwtTokenException(String msg) {
		super(msg);
	}
}


