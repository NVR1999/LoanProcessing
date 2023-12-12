package com.example.exceptionHandler;

public class UserNotFound extends Exception{
	
	public UserNotFound(String msg) {
		super(msg);
	}

}
