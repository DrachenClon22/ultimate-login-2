package com.drachenclon.dreg.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.drachenclon.dreg.AuthManager.AuthHandler;
import com.drachenclon.dreg.BanManager.BanHandler;
import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.Exceptions.StringCantBeValidatedException;
import com.drachenclon.dreg.FileManager.FileManager;
import com.drachenclon.dreg.FileManager.FileParser;
import com.drachenclon.dreg.HashManager.HashBuilder;
import com.drachenclon.dreg.MessageManager.MessageHandler;
import com.drachenclon.dreg.PlayerManager.PlayerRepo;
import com.drachenclon.dreg.PlayerManager.Hash.HashService;
import com.drachenclon.dreg.PlayerManager.Hash.PlayerHashInfo;
import com.drachenclon.dreg.ValidatorManager.ValidationResult;
import com.drachenclon.dreg.ValidatorManager.Validator;

/* 
 * Password command used for changing user settings.
 * 
 * Usage:
 * (1) /password change [from] [to] [to] - sets password for user himself
 * (2) /password user [user] set [to] - sets password for specific user
 * (3) /password user [user] delete - remove user from database
 * (4) /password logout - logging out of system
*/ 
public class PasswordCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
		}
		String message = "";
		
		if (!cmd.testPermission(sender)) {
			message = LanguageReader.GetLine("no_permission");
			MessageHandler.SendMessageFormat(player, message);
			return true;
		}
		
		if (args.length > 0) {
			try {
				switch (args[0]) {
				case ("logout"): {
					if (player != null) {
						AuthHandler.Logout(player, false);
					} else {
						System.out.println("Command cannot be used from console");
					}

					message = LanguageReader.GetLine("logged_out");
					MessageHandler.SendMessageFormat(player, message);
					return true;
				}
				case ("change"): {
					if (player != null) {
						PlayerHashInfo hash = new PlayerHashInfo(FileParser.GetPlayerInfo(player.getName()));
						if (!AuthHandler.CheckPassword(args[1], hash)) {
							AuthHandler.Logout(player, true);
							return false;
						}
						
						ValidationResult result = Validator.ValidatePassword(new String[] {args[2], args[3]});
						
						if (result.Result) {
							hash.SetPasswordHash(HashBuilder.GetStringHash(args[2] + hash.GetSaltAsString()));
							HashService.UpdateHash(null, hash);
							message = LanguageReader.GetLine("password_self_changed");
						} else {
							message = result.Message;
						}
						
						MessageHandler.SendMessageFormat((Player)sender, message);
						return true;
					} else {
						System.out.println("Command cannot be used from console");
					}
					break;
				}
				case ("user"): {
					switch (args[2]) {
					case ("set"): {
						
						boolean isTargetOp = PlayerRepo.IsPlayerOpped(args[1]);
						
						if (!sender.hasPermission("login.set.other") ||
								(isTargetOp && !sender.hasPermission("login.set.admin"))) {
							message = LanguageReader.GetLine("no_permission");
							MessageHandler.SendMessageFormat((Player)sender, message);
							return true;
						}
						
						if (sender instanceof Player) {
							if (((Player)sender).getName().equals(args[1])) {
								message = LanguageReader.GetLine("to_set_yourself");
								MessageHandler.SendMessageFormat((Player)sender, message);
								return true;
							}
						}
						
						PlayerHashInfo hash = null;
						
						try {
							hash = new PlayerHashInfo(FileParser.GetPlayerInfo(args[1]));
						} catch (StringCantBeValidatedException e) {
							message = LanguageReader.GetLine("user_does_not_exist")
									.replace("{user}", args[1]);;
							MessageHandler.SendMessageFormat((Player)sender, message);
							return true;
						}
						
						String newPassword = args[3] + hash.GetSaltAsString();
						ValidationResult result = Validator.ValidatePassword(args[3]);
						
						if (result.Result) {
							hash.SetPasswordHash(HashBuilder.GetStringHash(newPassword));
							HashService.UpdateHash(null, hash);
							message = LanguageReader.GetLine("password_user_changed")
									.replace("{changed_player}", args[1]);
							AuthHandler.Logout(args[1], true);
						} else {
							message = result.Message;
						}
						
						MessageHandler.SendMessageFormat((Player)sender, message);
						return true;
					}
					case ("delete"): {
						if (sender.hasPermission("login.delete.users")) {
							String hash = FileParser.GetPlayerInfo(args[1]);
							if (hash != null) {
								FileManager.TryDeleteFromFile(hash);
								message = LanguageReader.GetLine("user_deleted_success")
										.replace("{user}", args[1]);
							} else {
								message = LanguageReader.GetLine("user_does_not_exist")
										.replace("{user}", args[1]);
							}
						} else {
							message = LanguageReader.GetLine("no_permission");
						}
						MessageHandler.SendMessageFormat((Player)sender, message);
						
						Player target = PlayerRepo.GetPlayer(args[1]);
						if (target != null) {
							message = LanguageReader.GetLine("logged_out");
							BanHandler.Kick(target, message);
						}
						
						return true;
					}
					}
					break;
				}
				}
			}
			catch (IndexOutOfBoundsException e) {
				if (sender instanceof Player) {
					MessageHandler.SendMessageFormat((Player)sender, LanguageReader.GetLine("specify_args"));
				}
			}
			catch (Exception e) {
				if (sender instanceof Player) {
					MessageHandler.SendMessageFormat((Player)sender, LanguageReader.GetLine("error")
							.replace("{error_num}", "DE0001"));
				}
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
}
