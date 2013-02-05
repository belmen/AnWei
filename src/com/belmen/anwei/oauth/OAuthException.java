package com.belmen.anwei.oauth;

public class OAuthException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OAuthException(String message, Exception e) {
		super(message, e);
	}
	
	public OAuthException(String message) {
		super(message, null);
	}
}
