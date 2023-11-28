package com.drachenclon.dreg.EventManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.MessageManager.MessageHandler;
import com.drachenclon.dreg.PlayerManager.PlayerInstance;
import com.drachenclon.dreg.PlayerManager.PlayerRepo;
import com.drachenclon.dreg.PlayerManager.Hash.PlayerHashInfo;
import com.drachenclon.dreg.ValidatorManager.Validator;

/**
 * Class contains all events that should be cancelled if player
 * is not registered or logged in.
 */
public class CancellationEventsHandler implements Listener {
	
	private void HandleEvents(PlayerEvent event) {
		Player player = event.getPlayer();
		PlayerInstance player_instance = PlayerRepo.GetPlayerInstance(player);
		if (player_instance == null) {
			return;
		}
		
		if (event instanceof Cancellable) {
			((Cancellable) event).setCancelled(true);
		}
		
		String message = "";
		if (player_instance.GetHashInfo() == null) {
			message = LanguageReader.GetLine("use_register_chat");
		} else {
			message = LanguageReader.GetLine("use_login_chat");
		}
		MessageHandler.ClearChat(player);
		MessageHandler.SendMessageWithConfigValue(player, message);
	}
	
	@EventHandler
	public void OnPlayerMove(PlayerMoveEvent event) {
		HandleEvents(event);
	}
	
	@EventHandler
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (!Validator.StringContainsAny(event.getMessage(), new String[] { "/login", "/register" })) {
			HandleEvents(event);
		}
	}
	
	@EventHandler
	public void OnPlayerChat(AsyncPlayerChatEvent event) {
		HandleEvents(event);
	}
	
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event) {
		HandleEvents(event);
	}
	
	@EventHandler
	public void OnPlayerBreak(PlayerHarvestBlockEvent event) {
		HandleEvents(event);
	}
	
	@EventHandler
	public void OnPlayerDamage(PlayerItemDamageEvent event) {
		HandleEvents(event);
	}
	
	@EventHandler
	public void OnPlayerDropItem(PlayerDropItemEvent event) {
		HandleEvents(event);
	}
}
