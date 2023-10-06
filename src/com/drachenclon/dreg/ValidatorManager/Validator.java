package com.drachenclon.dreg.ValidatorManager;

import com.drachenclon.dreg.ConfigManager.ConfigReader;
import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.HashManager.HashBuilder;

public final class Validator {
	
	private static final String PROHIBITED_CHARS_FOR_PASSWORD = "\\|/'\"\' ";
	
	public static boolean TryValidateHash(String hash) {
		if (hash == null) {
			return false;
		}
		int blockSize = HashBuilder.GetBlockSize();
		
		// Last 2 bytes contain config info
		return (hash.length() - 2)%blockSize==0;
	}
	
	public static ValidationResult ValidatePassword(String args) {
		return ValidatePassword(new String[] {args, args});
	}
	public static ValidationResult ValidatePassword(String[] args) {
		String message = "";
		String password = "";
		
		if (!args[0].equals(args[1])) {
			message = LanguageReader.GetLine("should_be_same");
			return new ValidationResult(message, false);
		}
		
		password = args[0];
		
		if (!Validator.DoesStringContain(password)) {
			message = LanguageReader.GetLine("special_symbols_deny");
			return new ValidationResult(message, false);
		}
		
		int minLength = Integer.parseInt(ConfigReader.GetConfigValue("min-pass-length"));
		minLength = minLength < 5 ? 5 : minLength;
		
		if (password.length() < minLength) {
			message = LanguageReader.GetLine("password_too_short");
			return new ValidationResult(message, false);
		}
		
		int maxLength = Integer.parseInt(ConfigReader.GetConfigValue("max-pass-length"));
		maxLength = maxLength > 255 ? 255 : maxLength;
		
		if (password.length() > maxLength) {
			message = LanguageReader.GetLine("password_too_long");
			return new ValidationResult(message, false);
		}
		
		return new ValidationResult(null, true);
	}
	
	public static boolean DoesStringContain(String password) {
		return !StringContainsAny(password, PROHIBITED_CHARS_FOR_PASSWORD.toCharArray());
	}
	
	public static boolean StringContainsAny(String input, String[] params) {
		for(String str : params) {
			if (input.contains(str)) {
				return true;
			}
		}
		return false;
	}
	public static boolean StringContainsAny(String input, char[] params) {
		for(char chr : params) {
			if (input.contains(String.valueOf(chr))) {
				return true;
			}
		}
		return false;
	}
}
