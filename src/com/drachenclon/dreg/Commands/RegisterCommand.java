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
import com.drachenclon.dreg.ValidatorManager.ValidationResult;
import com.drachenclon.dreg.ValidatorManager.Validator;

/*
 * Used to allow people to register in system. Only used
 * if current user hasn't been found in database.
 * 
 * Usage:
 * (1) /register [password] [password]
 */
public class RegisterCommand implements CommandExecutor {

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
			
			if (args.length > 1) {
				ValidationResult result = Validator.ValidatePassword(args);
				
				if (result.Result) {
					if (AuthHandler.TryRegister(player, args[0])) {
						message = LanguageReader.GetLine("success");
						PlayerRepo.RemovePlayer(player);
					} else {
						PlayerHashInfo hash = PlayerRepo.GetPlayerInstance(player).GetHashInfo();
						if (hash != null) {
							int maxAttempts = Integer.parseInt(ConfigReader.GetConfigValueRaw("max-attempts"));
							message = LanguageReader.GetLocalizedLine("already_registered", player.getLocale()).replace("{attempts}", 
									String.valueOf(maxAttempts - hash.GetCurrentAttempts() + 1));
							MessageHandler.SendMessageWithFormat(player, message);
							
							return true;
						} else {
							message = LanguageReader.GetLine("specify_args");
						}
					}
				} else {
					message = result.Message;
					
					if (result.ErrorCode != null) {
						MessageHandler.SendErrorMessage(player, "error", "ERRCODE: " + result.ErrorCode + "; " + message);
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
