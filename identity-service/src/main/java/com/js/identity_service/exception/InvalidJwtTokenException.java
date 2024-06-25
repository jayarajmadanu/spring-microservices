package com.js.identity_service.exception;

public class InvalidJwtTokenException extends Exception{

	public InvalidJwtTokenException(String msg) {
		super(msg);
	}
}
