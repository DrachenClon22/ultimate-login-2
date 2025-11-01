package com.drachenclon.dreg.EventManager;

import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.MessageManager.MessageHandler;
import com.drachenclon.dreg.PlayerManager.PlayerInstance;
import com.drachenclon.dreg.PlayerManager.PlayerRepo;
import com.drachenclon.dreg.ValidatorManager.Validator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;

public class CancellationEventsHandler implements Listener {

	private static final String[] ALLOWED_CMDS = new String[]{ //"/l", "/reg"
			"/login",
			"/register"
	};

	private boolean shouldProtect(Player player) {
		if (player == null) return false;
		PlayerInstance inst = PlayerRepo.GetPlayerInstance(player);
		return inst != null;
	}

	private void messagePlayerToAuth(Player player) {
		if (player == null) return;
		String msg = (PlayerRepo.GetPlayerInstance(player).GetHashInfo() == null)
				? LanguageReader.GetLine("use_register_chat")
				: LanguageReader.GetLine("use_login_chat");
		MessageHandler.ClearChat(player);
		MessageHandler.SendMessageWithConfigValue(player, msg);
	}

	private void handlePlayerEvent(Event event, Player player) {
		if (!shouldProtect(player)) return;
		if (event instanceof Cancellable c) c.setCancelled(true);
		messagePlayerToAuth(player);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPlayerMove(PlayerMoveEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPlayerChat(AsyncPlayerChatEvent event) {
		String msg = event.getMessage().toLowerCase();
		if (Validator.StringContainsAny(msg, new String[]{"/login", "/register"})) return;
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPlayerInteract(PlayerInteractEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPlayerInteractEntity(PlayerInteractEntityEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnSwapHands(PlayerSwapHandItemsEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnItemHeld(PlayerItemHeldEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnItemConsume(PlayerItemConsumeEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnEditBook(PlayerEditBookEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnBedEnter(PlayerBedEnterEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnBucket(PlayerBucketEmptyEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnBucket(PlayerBucketFillEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnHarvest(PlayerHarvestBlockEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnBlockBreak(BlockBreakEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnBlockPlace(BlockPlaceEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPlayerItemDamage(PlayerItemDamageEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPlayerDropItem(PlayerDropItemEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player p)) return;
		handlePlayerEvent(event, p);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnInventoryDrag(InventoryDragEvent event) {
		if (!(event.getWhoClicked() instanceof Player p)) return;
		handlePlayerEvent(event, p);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnInventoryOpen(InventoryOpenEvent event) {
		if (!(event.getPlayer() instanceof Player p)) return;
		handlePlayerEvent(event, p);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnTeleport(PlayerTeleportEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPortal(PlayerPortalEvent event) {
		handlePlayerEvent(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		if (!shouldProtect(p)) return;
		String msg = event.getMessage().toLowerCase();
		boolean allowed = Arrays.stream(ALLOWED_CMDS).anyMatch(cmd -> msg.startsWith(cmd + " ") || msg.equals(cmd));
		if (allowed) return;
		event.setCancelled(true);
		messagePlayerToAuth(p);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnAnyDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player p)) return;
		if (!shouldProtect(p)) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnDamageByEntity(EntityDamageByEntityEvent event) {
		Entity victim = event.getEntity();
		if (victim instanceof Player p && shouldProtect(p)) {
			event.setCancelled(true);
			return;
		}
		Entity damager = event.getDamager();
		Player attacker = null;
		if (damager instanceof Player) attacker = (Player) damager;
		else if (damager instanceof org.bukkit.entity.Projectile proj) {
			ProjectileSource src = proj.getShooter();
			if (src instanceof Player pl) attacker = pl;
		}
		if (attacker != null && shouldProtect(attacker)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player p && shouldProtect(p)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnTargetLiving(EntityTargetLivingEntityEvent event) {
		if (event.getTarget() instanceof Player p && shouldProtect(p)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnFood(FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player p)) return;
		if (!shouldProtect(p)) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void OnPotionSplash(PotionSplashEvent event) {
		event.getAffectedEntities().removeIf(e -> e instanceof Player p && shouldProtect(p));
	}
}
