package com.example.demo.exception;

public class RoomTypeNotFoundException extends RuntimeException {
	public RoomTypeNotFoundException(String message) {
		super(message);
	}

}
