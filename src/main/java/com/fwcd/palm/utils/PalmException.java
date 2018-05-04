package com.fwcd.palm.utils;

public class PalmException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PalmException(Exception rethrow) {
		super(rethrow);
	}

	public PalmException(String message) {
		super(message);
	}
}