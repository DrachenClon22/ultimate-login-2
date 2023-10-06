package com.drachenclon.dreg.PlayerManager.Hash;

import com.drachenclon.dreg.ConfigManager.ConfigReader;
import com.drachenclon.dreg.ConfigManager.LanguageReader;
import com.drachenclon.dreg.Exceptions.StringCantBeValidatedException;
import com.drachenclon.dreg.FileManager.FileManager;
import com.drachenclon.dreg.PlayerManager.PlayerInstance;

public final class HashService {
	public static void UpdateHash(PlayerInstance playerInstance, PlayerHashInfo hashInfo) {
		if (playerInstance != null) {
			try {
				playerInstance.SetHashInfo(new PlayerHashInfo(hashInfo.GetCurrentHash()));
			} catch (StringCantBeValidatedException e) {
				String message = LanguageReader.GetLine("error").replace("{error_num}", "HS0000");
				playerInstance.GetPlayer().kickPlayer(message);
				e.printStackTrace();
			}
		}
		FileManager.TryReplaceInFile(hashInfo.GetStartedFullHash(), hashInfo.GetCurrentHash());
	}
	
	/**
	 * Check available attempts for user's hash
	 * @param hashInfo Hash of the current user
	 * @return false if user has 0 attempts left, true if user has more
	 * than 0 attempts (1 or more)
	 */
	public static boolean CheckAttempts(PlayerHashInfo hashInfo) {
		int maxAttempts = Integer.parseInt(ConfigReader.GetConfigValue("max-attempts"));
		int currentAttempts = hashInfo.GetCurrentAttempts();
		
		if (currentAttempts > maxAttempts) {
			return false;
		}
		
		return true;
	}
}
