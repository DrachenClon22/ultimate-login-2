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
import com.drachenclon.dreg.PlayerManager.PlayerRepo;
import com.drachenclon.dreg.ValidatorManager.Validator;

/**
 * Class contains all events that should be cancelled if player
 * is not registered or logged in.
 */
public class CancellationEventsHandler implements Listener {
	
	private void HandleEvents(PlayerEvent event) {
		Player player = event.getPlayer();
		
		if (PlayerRepo.GetPlayerInstance(player) == null) {
			return;
		}
		
		if (event instanceof Cancellable) {
			((Cancellable) event).setCancelled(true);
		}
		String message = LanguageReader.GetLine("not_logged_in");
		MessageHandler.SendMessageFormat(player, message);
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
