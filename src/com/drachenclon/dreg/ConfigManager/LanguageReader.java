package com.drachenclon.dreg.ConfigManager;

public final class LanguageReader {
	// TODO: Stub for getting language line, should get from lang.yml
	@SuppressWarnings("deprecation")
	public static String GetLine(String param) {
		return ConfigReader.GetMessage(param);
	}
}
