package com.drachenclon.dreg;

import com.drachenclon.dreg.Commands.TabCompleter.NoTabCompleter;
import com.drachenclon.dreg.VersionHandler.Version;
import com.drachenclon.dreg.VersionHandler.VersionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.drachenclon.dreg.Commands.LoginCommand;
import com.drachenclon.dreg.Commands.PasswordCommand;
import com.drachenclon.dreg.Commands.RegisterCommand;
import com.drachenclon.dreg.ConfigManager.ConfigReader;
import com.drachenclon.dreg.EventManager.CancellationEventsHandler;
import com.drachenclon.dreg.FileManager.FileBuilder;
import com.drachenclon.dreg.HashManager.HashBuilder;
import com.drachenclon.dreg.HashManager.HashBuilders.SHA256Builder;
import com.drachenclon.dreg.LoggerManager.LoggerCensor;
import com.drachenclon.dreg.MessageManager.MessageHandler;
import com.drachenclon.dreg.PlayerManager.PlayerRepo;

import java.util.logging.Level;

/*
 * Please, be aware that comments may be deprecated
 */
public class UltimateLogin extends JavaPlugin {
	
	// Picture to draw attention
	public static final String SALT = "89bc1dde7cd2d573139223e0c4dc55bed48ddc42c61a8e57c0e6aa63f4540fb2";
	
	/*
	 * All Hash info about players stores in a file and separated on 5 different blocks of
	 * information.
	 * 
	 * (1) First block contains hashed info about user nickname.
	 * (2) Second block contains hashed password+salt (this may not be exactly password+salt
	 * in this or future versions, but this is the main idea).
	 * (3) Third block contains raw salt itself, may not be the best approach, but still okay.
	 * (4) Fourth block contains hashed ip+salt address so user can be joining server without needing to
	 * enter /login command every time. Maybe a variable should be stored and the user wants to type 
	 * /login every time they join? Who knows.
	 * (5) Not a block actually, but 2 bytes of info about remaining attempts for user and user config.
	 * 1st byte is char value of attempts (0-255), 2nd byte is some config, not yet for something (Ver 2.0.1).
	 * If user ran out of attempts then they get banned and attempts resets back to 0.
	 * 
	 * So the scheme of blocks is something like this:
	 * H(USERNAME) H(PASS+SALT) SALT H(IP) ATTEMPTS+CONFIG(2 bytes)
	 */
	@Override
	public void onEnable() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		saveDefaultConfig();
		
		// Before managing players list all stuff should be initiated to work with.
		try {
			/*
			 * Is is recommended that MessageHandler comes first in case of any
			 * errors that may occur in init of any class. MessageHandler can be used to notify
			 * opped players that problem has occur.
			 */
			MessageHandler.init(this);
			LoggerCensor.init(this);
			ConfigReader.init(this);
			FileBuilder.init(getDataFolder(), "accounts");
			HashBuilder.init(new SHA256Builder());
			PlayerRepo.init(this);
			VersionManager.init(this);
		} catch (Exception e) {
			/*
			 * If something went wrong during startup, all op players will receive the message.
			 * Or in worst scenario all players will receive error message, this is why
			 * message handler init comes first.
			 */
			try {
				MessageHandler.BroadcastFormat("&c[UltimateLogin2] Error on start, check console for more info.", true);
			} catch (Exception j) {
				j.printStackTrace();
				e.printStackTrace();
				Bukkit.broadcastMessage(e.getMessage());
			}
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		try {
			if (VersionManager.checkVersion()) {
				this.getLogger().log(Level.INFO,
						"You are using the latest version!");
			} else {
				this.getLogger().log(Level.WARNING,
						"Newest version " + VersionManager.getCurrentVersionFromNet() + " available! Download at https://www.spigotmc.org/resources/ultimate-login.112969/ or https://github.com/DrachenClon22/ultimate-login-2/releases");
			}
		} catch (Exception e) {
			this.getLogger().log(Level.WARNING,
					"Cannot check for latest version. Probably offline?");
		}

		/*
		 * Register listeners
		 * PlayerRepo listens for players joining and quitting events so they can be added
		 * in the player list to freeze or whatever them later.
		 */
		getServer().getPluginManager().registerEvents(new PlayerRepo(), this);
		getServer().getPluginManager().registerEvents(new CancellationEventsHandler(), this);
		
		RegisterCommands();
		
		// Add all players to unauth list on startup
		PlayerRepo.AddAllPlayers();
	}
	
	@Override
	public void onDisable() {
		/* 
		 * Always remove all players from repo when shutting down or reloading plugin or server
		 * or else there can be some players that shouldn't have access to server but still have it.
		 * Or in other words not removing all players from list may cause bugs.
		 */
		PlayerRepo.RemoveAllPlayers();
	}

	public void RegisterCommands() {
		this.getCommand("register").setExecutor(new RegisterCommand());
		this.getCommand("register").setTabCompleter(NoTabCompleter.INSTANCE);

		this.getCommand("login").setExecutor(new LoginCommand());
		this.getCommand("login").setTabCompleter(NoTabCompleter.INSTANCE);

		this.getCommand("password").setExecutor(new PasswordCommand(this));
		this.getCommand("password").setTabCompleter(NoTabCompleter.INSTANCE);
	}
}
