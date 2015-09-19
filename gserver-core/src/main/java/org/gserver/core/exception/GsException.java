package org.gserver.core.exception;

public class GsException extends Exception {

	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String message;

	public GsException(int errorCode) {
		this.errorCode = errorCode;
	}

	public GsException(int errorCode, String message) {
		super(message);
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
