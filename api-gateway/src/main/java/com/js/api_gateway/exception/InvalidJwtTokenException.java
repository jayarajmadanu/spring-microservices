package com.js.api_gateway.exception;

public class InvalidJwtTokenException extends Exception{

	public InvalidJwtTokenException(String msg) {
		super(msg);
	}
}
