package com.drachenclon.dreg.MessageManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.drachenclon.dreg.UltimateLogin;
import com.drachenclon.dreg.Exceptions.NoPluginException;

/**
 * Class used for handling any message sending and broadcasting.
 * <br>
 * Always use {@link #init(plugin)} before calling any
 * method from this class.
 */
public final class MessageHandler {
	private static UltimateLogin _plugin;
	
	/**
	 * Uses for init main plugin for forking with players and messages.
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
	 * Sends message to player in formatted style.
	 * @param player is the player that should receive message
	 * @param message text that should be sent to player
	 * @see {@link #BroadcastFormat(String)}
	 */
	public static void SendMessageFormat(Player player, String message) {
		if (player != null) {
			player.sendMessage(GetMessageFormat(message));
		}
	}
	
	/**
	 * Used for broadcasting message to users in formatted style.
	 * <br>
	 * Always use {@link #init(plugin)} before calling any
	 * method from this class.
	 * @see {@link #BroadcastFormat(String, boolean)}
	 * @param message text that sending to users
	 * @throws NoPluginException throwing when main plugin is null
	 */
	public static void BroadcastFormat(String message) throws NoPluginException {
		if (_plugin == null) {
			throw new NoPluginException("Main plugin variable is null. Use init(plugin).");
		}
		for(Player pl : _plugin.getServer().getOnlinePlayers()) {
			SendMessageFormat(pl, message);
		}
	}
	/**
	 * Used for broadcasting message to <strong>only opped</strong> users in formatted style.
	 * <br>
	 * Always use {@link #init(plugin)} before calling any
	 * method from this class.
	 * @see {@link #BroadcastFormat(String)}
	 * @param message text that sending to users
	 * @param onlyForOp uses when message should be sent only to opped players
	 * @throws NoPluginException throwing when main plugin is null
	 */
	public static void BroadcastFormat(String message, boolean onlyForOp) throws NoPluginException {
		if (!onlyForOp) {
			BroadcastFormat(message);
		}
		if (_plugin == null) {
			throw new NoPluginException("Main plugin variable is null. Use init(plugin).");
		}
		for(Player pl : _plugin.getServer().getOnlinePlayers()) {
			if (pl.isOp()) {
				SendMessageFormat(pl, message);
			}
		}
	}
	
	private static String GetMessageFormat(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
