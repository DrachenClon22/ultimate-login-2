package com.drachenclon.dreg.LoggerManager;

import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

import com.drachenclon.dreg.UltimateLogin;
import com.drachenclon.dreg.Exceptions.NoPluginException;
import com.drachenclon.dreg.ValidatorManager.Validator;

/**
 * Class for censoring console and log files from any unwanted or sensitive info.
 */
public class LoggerCensor {
	
	private static org.apache.logging.log4j.core.Logger _coreLogger;
	private static UltimateLogin _plugin;
	
	// Commands that should be silenced in console and logs.
	private static final String[] SILENT_COMMANDS = {
			"/login",
			"/register",
			//	"/password"
	};
	
	/*
	 * Commands that should be shown in console like "user issued command /command" without
	 * any details about arguments.
	 * Good example of usage is /password command, if admin used this, others can see
	 * this in logs later, but target password isn't shown in console or logs.
	 */
	private static final String[] WARN_COMMANDS = {
			"/password"
	};
	
	/**
	 * Init logger censor to hide unwanted messages from logs and console messages
	 * @param plugin core plugin
	 * @throws NoPluginException throws if input plugin is null
	 */
	public static void init(UltimateLogin plugin) throws NoPluginException {
		if (plugin == null) {
			throw new NoPluginException("Plugin is null.");
		}
		_plugin = plugin;
		_coreLogger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
		
		AddCoreFilter();
	}
	
	/*
	 * Only triggered if console message has "issued server command" that means
	 * user entered a command and ignores any other messages.
	 */
	private static void AddCoreFilter() {
		_coreLogger.addFilter(new AbstractFilter() {
			@Override
			public Filter.Result filter(LogEvent event) {
				String message = event.getMessage().getFormattedMessage();
				message = message.toLowerCase();
				
				if (message.contains("issued server command")) {
					if (Validator.StringContainsAny(message, SILENT_COMMANDS)) {
						if (Validator.StringContainsAny(message, WARN_COMMANDS)) {
							
							/*
							 * This code only triggers if some console message has
							 * "issued server command" text. but if the user writes this in the chat 
							 * on purpose or inadvertently, then the code will also work, but will 
							 * not be able to find the user and throw an error.
							 */
							try {
								String[] messageWords = message.split(" ");
								
								_plugin.getLogger().log(Level.WARNING, 
										"User " + messageWords[0] + " issued \"" 
								+ messageWords[4] + "\" command");
							} catch (Exception e) {
								return Filter.Result.NEUTRAL;
							}
						}
						
						return Filter.Result.DENY;
					}
				}
				
				return Filter.Result.NEUTRAL;
				}
			});
	}
}
