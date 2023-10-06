package com.drachenclon.dreg.AuthManager;

import org.bukkit.entity.Player;

import com.drachenclon.dreg.BanManager.BanHandler;
import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.Exceptions.StringCantBeValidatedException;
import com.drachenclon.dreg.FileManager.FileManager;
import com.drachenclon.dreg.FileManager.FileParser;
import com.drachenclon.dreg.HashManager.HashBuilder;
import com.drachenclon.dreg.PlayerManager.PlayerInstance;
import com.drachenclon.dreg.PlayerManager.PlayerRepo;
import com.drachenclon.dreg.PlayerManager.Hash.HashService;
import com.drachenclon.dreg.PlayerManager.Hash.PlayerHashInfo;
import com.drachenclon.dreg.SaltManager.SaltBuilder;

/**
 * Class for performing auth operations and calculations.
 */
public final class AuthHandler {
	
	/**
	 * @see {@link #TryAuthWithIp(username, ip)}
	 */
	public static boolean TryAuthWithIp(Player player) {
		return TryAuthWithIp(player.getName(), player.getAddress().getAddress().toString());
	}
	/**
	 * Try to auth given user only with nickname and ip address.
	 * Uses to not disturb the user by entering a password if the IP matches.
	 * @param username name of the user
	 * @param ip user's ip address
	 * @return True if user is in database and user's ip is correct.
	 * <br>False if user's ip in database not matching with user's current ip.
	 */
	public static boolean TryAuthWithIp(String username, String ip) {
		String usernameHash = HashBuilder.GetStringHash(username);
		String ipHash = HashBuilder.GetStringHash(ip);
		
		PlayerHashInfo hashInfo = PlayerRepo.GetPlayerInstance(username).GetHashInfo();
		
		if (hashInfo == null) {
			return false;
		}

		return (hashInfo.GetNicknameHashAsString().equals(usernameHash)) &&
				(hashInfo.GetIPHashAsString().equals(ipHash));
	}
	
	public static boolean TryRegister(Player player, String password) {
		return TryRegister(player.getName(), password, player.getAddress().getAddress().toString());
	}
	public static boolean TryRegister(String username, String password, String ip) {

		PlayerInstance playerInstance = PlayerRepo.GetPlayerInstance(username);
		
		// This was made for JUnit testing purposes, actual user always has PlayerInstance (not authed)
		if (playerInstance == null) {
			playerInstance = new PlayerInstance(null, null);
		}
		
		// If user already has HashInfo means user's already registered
		PlayerHashInfo hashInfo = playerInstance.GetHashInfo();
		if (hashInfo != null) {
			hashInfo.ReduceAttempts();
			HashService.UpdateHash(playerInstance, hashInfo);
			return false;
		}
		
		String ipHash = HashBuilder.GetStringHash(ip);
		return FileManager.TryWriteToFile(GenerateUserHash(
				username, 
				password, 
				SaltBuilder.GetRandomSaltString()
				) + ipHash + '\0' + '\0');
	}
	
	public static boolean TryAuthWithPassword(Player player, String password) {
		return TryAuthWithPassword(player.getName(), password);
	}
	public static boolean TryAuthWithPassword(String username, String password) {
		
		PlayerInstance playerInstance = PlayerRepo.GetPlayerInstance(username);
		PlayerHashInfo hashInfo = playerInstance.GetHashInfo();
		
		if (hashInfo == null) {
			return false;
		}
		
		Player player = PlayerRepo.GetPlayerInstance(username).GetPlayer();
		boolean result = CheckPassword(password, hashInfo);
		
		if (!result) {
			hashInfo.ReduceAttempts();
			
			if (!HashService.CheckAttempts(hashInfo)) {
				hashInfo.ResetAttempts();
				
				String message = LanguageReader.GetLine("attempts_zero");
				BanHandler.Ban(player, message);
			}
			
		} else {
			hashInfo.SetIpHash(HashBuilder.GetStringHash(player.getAddress().getAddress().toString()));
			hashInfo.ResetAttempts();
		}
		HashService.UpdateHash(playerInstance, new PlayerHashInfo(hashInfo));
		
		return result;
	}
	
	public static void Logout(String username, boolean kick) {
		PlayerHashInfo hashInfo = null;
		try {
			/*
			 * Getting hash from file directly so offline players can be logged out too
			 */
			hashInfo = new PlayerHashInfo(FileParser.GetPlayerInfo(username));
		} catch (StringCantBeValidatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hashInfo.SetIpHash(HashBuilder.GetStringHash("0"));
		HashService.UpdateHash(null, hashInfo);
		
		Player player = PlayerRepo.GetPlayer(username);
		if (player != null) {
			PlayerRepo.AddPlayer(player);	
			if (kick) {
				player.kickPlayer(LanguageReader.GetLine("logged_out"));
			}
		}
	}
	public static void Logout(Player player, boolean kick) {
		Logout(player.getName(), kick);
	}
	
	public static boolean CheckPassword(String rawPassword, PlayerHashInfo hashInfo) {
		String passwordHash = HashBuilder.GetStringHash(rawPassword + hashInfo.GetSaltAsString());	
		return hashInfo.GetPasswordHashAsString().equals(passwordHash);
	}
	
	/**
	 * Used for registration and auth hashed string generation. This string used for
	 * comparison with gathered string from database.
	 * @param username raw name of the user being registrated/authed
	 * @param password raw password of user
	 * @param salt raw salt, generated or gathered
	 * @return String with hashed user information
	 */
	private static String GenerateUserHash(String username, String password, String salt) {
		
		String usernameHash = HashBuilder.GetStringHash(username);
		String passwordHash = HashBuilder.GetStringHash(password + salt);
		
		return usernameHash + passwordHash + salt;
	}
}
