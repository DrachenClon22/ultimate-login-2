package com.drachenclon.dreg.ValidatorManager;

public final class ValidationResult {
	/**
	 * Message from validation script if result is false
	 */
	public final String Message;
	public final String ErrorCode;
	/**
	 * Validation result
	 */
	public final boolean Result;
	
	public ValidationResult(String message, boolean result, String error) {
		Message = message;
		Result = result;
		ErrorCode = error;
	}
	
	public ValidationResult(String message, boolean result) {
		Message = message;
		Result = result;
		ErrorCode = null;
	}
}
