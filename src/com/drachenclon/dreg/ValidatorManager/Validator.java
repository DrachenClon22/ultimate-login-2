package com.drachenclon.dreg.ValidatorManager;

import java.util.regex.Pattern;

import com.drachenclon.dreg.ConfigManager.ConfigReader;
import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.HashManager.HashBuilder;

public final class Validator {
	
	//private static final String PROHIBITED_CHARS_FOR_PASSWORD = "\\|/'\"\' ";
	private static final String PROHIBITED_CHARS_FOR_PASSWORD = " ";
	
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
		
		try {
			if (!args[0].equals(args[1])) {
				message = LanguageReader.GetLine("should_be_same");
				return new ValidationResult(message, false);
			}
			
			password = args[0];
			
			if (!Validator.DoesStringContain(password)) {
				message = LanguageReader.GetLine("special_symbols_deny");
				return new ValidationResult(message, false);
			}
			
			int minLength = ConfigReader.GetConfigValueInteger("min-pass-length");
			minLength = minLength < 5 ? 5 : minLength;
			
			if (password.length() < minLength) {
				message = LanguageReader.GetLine("password_too_short");
				return new ValidationResult(message, false);
			}
			
			int maxLength = ConfigReader.GetConfigValueInteger("max-pass-length");
			maxLength = maxLength > 255 ? 255 : maxLength;
			
			if (password.length() > maxLength) {
				message = LanguageReader.GetLine("password_too_long");
				return new ValidationResult(message, false);
			}
			
			if (ConfigReader.GetConfigValueBoolean("password-complexity-check")) {
				String searchLine = "password-complexity-demand";
				String regexLine = "";
				boolean uppercase = ConfigReader.GetConfigValueBoolean(searchLine+".uppercase");
				boolean lowercase = ConfigReader.GetConfigValueBoolean(searchLine+".lowercase");
				boolean numbers = ConfigReader.GetConfigValueBoolean(searchLine+".numbers");
				boolean non_alpha = ConfigReader.GetConfigValueBoolean(searchLine+".non-alpha-numeric");
				
				if (uppercase) {
					regexLine += "(?=.*?[A-Z])";
				}
				if (lowercase) {
					regexLine += "(?=.*?[a-z])";
				}
				if (numbers) {
					regexLine += "(?=.*?[0-9])";
				}
				if (non_alpha) {
					regexLine += "(?=.*?[#?!@$%^&*-])";
				}
				boolean isMatch = Pattern.compile(regexLine)
			               .matcher(password)
			               .find();
				if (!isMatch) {
					message = LanguageReader.GetLine("password_too_weak");
					return new ValidationResult(message, false);
				}
			}
		} catch (Exception e) {
			return new ValidationResult(e.getMessage(), false, "VALID-0001");
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
