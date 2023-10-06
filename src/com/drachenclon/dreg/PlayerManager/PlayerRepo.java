package com.drachenclon.dreg.PlayerManager;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.drachenclon.dreg.UltimateLogin;
import com.drachenclon.dreg.AuthManager.AuthHandler;
import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.Exceptions.StringCantBeValidatedException;
import com.drachenclon.dreg.FileManager.FileParser;
import com.drachenclon.dreg.MessageManager.MessageHandler;
import com.drachenclon.dreg.PlayerManager.Hash.PlayerHashInfo;

public final class PlayerRepo implements Listener {
	public static HashSet<PlayerInstance> UnauthPlayers = new HashSet<PlayerInstance>();
	
	private static UltimateLogin _plugin;
	
	public static void init(UltimateLogin plugin) {
		_plugin = plugin;
	}
	
	public static void ResetPlayers() {
		RemoveAllPlayers();
		AddAllPlayers();
	}
	
	public static boolean IsPlayerOpped(Player player) {
		return IsPlayerOpped(player.getName());
	}
	public static boolean IsPlayerOpped(String player) {
		Set<OfflinePlayer> opList = _plugin.getServer().getOperators();
		for (OfflinePlayer pl : opList) {
			if (pl.getName().equals(player)) {
				return true;
			}
		}
		return false;
	}
	
	public static Player GetPlayer(String username) {
		return _plugin.getServer().getPlayer(username);
	}
	
	public static void AddPlayer(String username) {
		Player player = GetPlayer(username);
		if (player != null) {
			AddPlayer(player);	
		}
	}
	
	public static void AddPlayer(Player player) {
		PlayerInstance instance = GetPlayerInstance(player);
		if (instance != null) {
			return;
		}
		PlayerHashInfo hashInfo = null;
		try {
			hashInfo = new PlayerHashInfo(FileParser.GetPlayerInfo(player));
		} catch (StringCantBeValidatedException e) { }
		
		UnauthPlayers.add(new PlayerInstance(player, hashInfo));
	}
	
	public static void RemovePlayer(Player player) {
		PlayerInstance instance = GetPlayerInstance(player);
		if (instance == null) {
			return;
		}
		UnauthPlayers.remove(instance);
	}
	
	public static void AddAllPlayers() {
		for (Player pl : _plugin.getServer().getOnlinePlayers()) {
			AddPlayer(pl);
			
			if (AuthHandler.TryAuthWithIp(pl)) {
				RemovePlayer(pl);
			}
		}
	}
	
	public static void RemoveAllPlayers() {
		UnauthPlayers.clear();
	}
	
	public static PlayerInstance GetPlayerInstance(Player player) {
		return GetPlayerInstance(player.getName());
	}
	
	public static PlayerInstance GetPlayerInstance(String username) {
		for (PlayerInstance pi : UnauthPlayers) {
			if (pi.GetPlayer().getName() == username) {
				return pi;
			}
		}
		return null;
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String message = "";
		
		if (!UnauthPlayers.contains(GetPlayerInstance(player))) {
			AddPlayer(player);
			
			if (AuthHandler.TryAuthWithIp(player)) {
				RemovePlayer(player);
				message = LanguageReader.GetLine("welcome_back").replace("{name}", player.getName());
			} else {
				PlayerHashInfo hash = GetPlayerInstance(player).GetHashInfo();
				if (hash == null) {
					message = LanguageReader.GetLine("use_register_chat");
				} else {
					message = LanguageReader.GetLine("use_login_chat");
				}
			}
			
			MessageHandler.SendMessageFormat(player, message);
		}
    }
	
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
		PlayerInstance player = GetPlayerInstance(event.getPlayer());
		
		if (UnauthPlayers.contains(player)) {
			UnauthPlayers.remove(player);
		}
    }
}
