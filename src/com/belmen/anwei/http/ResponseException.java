package com.belmen.anwei.http;

public class ResponseException extends HttpException {

	private static final long serialVersionUID = 1L;

	public ResponseException(Exception cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ResponseException(String msg, Exception cause, int statusCode) {
		super(msg, cause, statusCode);
		// TODO Auto-generated constructor stub
	}

	public ResponseException(String msg, Exception cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public ResponseException(String msg, int statusCode) {
		super(msg, statusCode);
		// TODO Auto-generated constructor stub
	}

	public ResponseException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
}