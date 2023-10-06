package com.drachenclon.dreg.Exceptions;

public class StringCantBeValidatedException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public StringCantBeValidatedException(String message) {
		super(message);
	}
}
