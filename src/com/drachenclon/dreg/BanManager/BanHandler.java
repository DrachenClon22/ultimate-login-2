package com.drachenclon.dreg.BanManager;

import java.net.InetAddress;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class BanHandler {
	@SuppressWarnings("deprecation")
	public static void Ban(Player player, String message) {
		Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), message, null, "UltimateLogin2");
		Kick(player, message);
	}
	
	@SuppressWarnings("deprecation")
	public static void Ban(String player, String message) {
		Bukkit.getBanList(BanList.Type.NAME).addBan(player, message, null, "UltimateLogin2");
	}
	
	public static void BanIp(Player player, String message) {
		InetAddress playerAddress = player.getAddress().getAddress();
		Bukkit.banIP(playerAddress);
		
		Ban(player, message);
	}
	
	public static void Kick(Player player, String message) {
		player.kickPlayer(message);
	}
}
