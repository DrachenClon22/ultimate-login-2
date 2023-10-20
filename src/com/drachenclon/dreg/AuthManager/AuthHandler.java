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
		PlayerHashInfo hashInfo;
		try {
			hashInfo = new PlayerHashInfo(FileParser.GetPlayerInfo(username));
		} catch (Exception e) {
			return false;
		}
		
		String usernameHash = HashBuilder.GetStringHash(username);
		String ipHash = HashBuilder.GetStringHash(ip + hashInfo.GetSaltAsString());

		return (hashInfo.GetNicknameHashAsString().equals(usernameHash)) &&
				(hashInfo.GetIPHashAsString().equals(ipHash));
	}
	
	public static boolean TryRegister(Player player, String password) {
		return TryRegister(player.getName(), password, player.getAddress().getAddress().toString());
	}
	public static boolean TryRegister(String username, String password, String ip) {
		
		// If user already has HashInfo means user's already registered
		PlayerHashInfo hashInfo = null;
		String playerInfo = FileParser.GetPlayerInfo(username);
		if (playerInfo != null) {
			try {
				hashInfo = new PlayerHashInfo(playerInfo);
			} catch (StringCantBeValidatedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (hashInfo != null) {
				hashInfo.ReduceAttempts();
				HashService.UpdateHash(hashInfo);
				return false;
			}
		}
		
		String salt = SaltBuilder.GetRandomSaltString();
		String ipHash = HashBuilder.GetStringHash(ip + salt);
		return FileManager.TryWriteToFile(GenerateUserHash(
				username, 
				password, 
				salt
				) + ipHash + '0' + '0');
	}
	
	public static boolean TryAuthWithPassword(Player player, String password) {
		return TryAuthWithPassword(player.getName(), password);
	}
	public static boolean TryAuthWithPassword(String username, String password) {
		
		//PlayerInstance playerInstance = PlayerRepo.GetPlayerInstance(username);
		PlayerHashInfo hashInfo;
		try {
			hashInfo = new PlayerHashInfo(FileParser.GetPlayerInfo(username));
		} catch (StringCantBeValidatedException e) {
			return false;
		}
		boolean result = CheckPassword(password, hashInfo);
		
		PlayerInstance player = PlayerRepo.GetPlayerInstance(username);
		if (!result) {
			hashInfo.ReduceAttempts();
			
			if (!HashService.CheckAttempts(hashInfo)) {
				hashInfo.ResetAttempts();
				
				try {
					String message = LanguageReader.GetLine("attempts_zero");
					BanHandler.Ban(username, message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} else {
			if (player != null) {
				hashInfo.SetIpHash(HashBuilder.GetStringHash(player.GetPlayer().getAddress().getAddress().toString()
						+ hashInfo.GetSaltAsString()));
			}
			hashInfo.ResetAttempts();
		}
		if (player != null) {
			HashService.UpdateHash(player, new PlayerHashInfo(hashInfo));
		} else {
			HashService.UpdateHash(new PlayerHashInfo(hashInfo));
		}
		
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
