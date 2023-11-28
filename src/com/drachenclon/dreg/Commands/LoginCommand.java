package com.drachenclon.dreg.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drachenclon.dreg.AuthManager.AuthHandler;
import com.drachenclon.dreg.ConfigManager.ConfigReader;
import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.MessageManager.MessageHandler;
import com.drachenclon.dreg.PlayerManager.PlayerRepo;
import com.drachenclon.dreg.PlayerManager.Hash.PlayerHashInfo;

/*
 * Uses for login player on server if any suspicious activity spotted
 * (like different ip). If user fails to enter login several times user
 * gets banned. If user successfully enters password, all attempts resets to 0.
 * 
 * Usage:
 * (1) /login [password] - used for login player to server
 */
public class LoginCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			String message = "";
			
			if (!cmd.testPermission(sender)) {
				message = LanguageReader.GetLine("no_permission");
				MessageHandler.SendMessageWithConfigValue(player, message);
				return true;
			}
			
			if (PlayerRepo.GetPlayerInstance(player) == null) {
				message = LanguageReader.GetLine("already_logged");
				MessageHandler.SendMessageWithConfigValue(player, message);
				return true;
			}
			
			PlayerHashInfo hash = PlayerRepo.GetPlayerInstance(player).GetHashInfo();
			if (args.length > 0) {
				if (AuthHandler.TryAuthWithPassword(player, args[0])) {
					message = LanguageReader.GetLine("welcome_back");
					PlayerRepo.RemovePlayer(player);
				} else {	
					if (hash == null) {
						message = LanguageReader.GetLine("use_register_chat");
					} else {
						int maxAttempts = Integer.parseInt(ConfigReader.GetConfigValueRaw("max-attempts"));
						int attempts = maxAttempts - hash.GetCurrentAttempts();
						
						message = LanguageReader.GetLocalizedLine("wrong_password", player.getLocale()).replace("{attempts}", 
								String.valueOf(attempts));
						
						MessageHandler.SendMessageWithFormat(player, message);
						return true;
					}
				}
				
				MessageHandler.SendMessageWithConfigValue(player, message);
				return true;
			}
			
		}
		return false;
	}

}
