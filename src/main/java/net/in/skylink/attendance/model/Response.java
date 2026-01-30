package net.in.skylink.attendance.model;

import org.springframework.http.HttpStatusCode;

public class Response<T> {
	
	private HttpStatusCode statusCode;
	
	private boolean success;
	
	private T data;

}
