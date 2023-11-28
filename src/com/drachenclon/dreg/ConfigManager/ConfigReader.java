package com.drachenclon.dreg.ConfigManager;

import com.drachenclon.dreg.UltimateLogin;
import com.drachenclon.dreg.Exceptions.NoPluginException;

/**
 * Class for reading main config.yml file
 */
public final class ConfigReader {
	
	private static UltimateLogin _plugin;
	
	/**
	 * Used to init main plugin to work with config file.
	 * @param plugin is the main plugin class (UltimateLogin)
	 * @throws NoPluginException if core plugin is null
	 */
	public static void init(UltimateLogin plugin) throws NoPluginException {
		if (plugin == null) {
			throw new NoPluginException("Main plugin variable is null");
		}
		_plugin = plugin;
	}
	
	/**
	 * Read value from config.yml file
	 * @param param value name parameter
	 * @return String with value of parameter
	 */
	public static String GetConfigValueRaw(String param) {
		return _plugin.getConfig().getString(param);
	}
	
	public static boolean GetConfigValueBoolean(String param) {
		try {
			return Boolean.parseBoolean(GetConfigValueRaw(param));
		} catch (Exception e) {
			return false;
		}
	}
	
	public static int GetConfigValueInteger(String param) {
		try {
			return Integer.parseInt(GetConfigValueRaw(param));
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * [Not recommended] Get config string from config.yml file
	 * @param param parameter with message line
	 * @return String with config value. E.g. return can be: lang.en.helloworld
	 */
	// TODO: This should be in lang.yml file, not config.yml
	@Deprecated
	public static String GetValueMessage(String param) {
		
		return param;
	}
	
	/**
	 * [Not recommended] Get localized message from config.yml file
	 * @param param parameter with message line
	 * @return String with localized message
	 */
	// TODO: This should be in lang.yml file, not config.yml
	@Deprecated
	public static String GetLocalizedMessage(String param) {
		return GetConfigValueRaw("lang." + GetConfigValueRaw("language") + "." + param);
	}
	
	/**
	 * [Not recommended] Get localized message from config.yml file
	 * @param param parameter with message line
	 * @return String with localized message
	 */
	// TODO: This should be in lang.yml file, not config.yml
	@Deprecated
	public static String GetLocalizedMessage(String param, String locale) {
		String language = GetConfigValueRaw("language");
		try {
			if (Boolean.parseBoolean(GetConfigValueRaw("language-detection"))) {
				if (_plugin.getConfig().contains("lang." + locale.substring(0, 2))) {
					language = locale.substring(0, 2);
					if (_plugin.getConfig().contains("lang." + locale)) {
						language = locale;
					}
				}
			}
		} catch (Exception e) {
			language = GetConfigValueRaw("language");
		}
		return GetConfigValueRaw("lang." + language + "." + param);
	}
}
