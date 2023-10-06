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
	public static String GetConfigValue(String param) {
		return _plugin.getConfig().getString(param);
	}
	
	/**
	 * [Not recommended] Get localized message from config.yml file
	 * @param param parameter with message line
	 * @return String with localized message
	 */
	// TODO: This should be in lang.yml file, not config.yml
	@Deprecated
	public static String GetMessage(String param) {
		return _plugin.getConfig().getString("lang." + GetConfigValue("language") + "." + param);
	}
}
