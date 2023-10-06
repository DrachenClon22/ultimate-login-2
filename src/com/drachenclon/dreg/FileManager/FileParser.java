package com.drachenclon.dreg.FileManager;

import org.bukkit.entity.Player;

import com.drachenclon.dreg.HashManager.HashBuilder;

public class FileParser {
	public static String GetPlayerInfo(String username) {
		String content = FileManager.ReadFile();
		if (content == null) {
			return null;
		}
		int start = content.indexOf(HashBuilder.GetStringHash(username));	
		
		if (start == -1) {
			return null;
		}
		
		int finish = start + (HashBuilder.GetBlockSize() * 4 + 2);
		
		if (content.length() < finish) {
			return null;
		}
		
		return content.substring(start, finish);
	}
	
	public static String GetPlayerInfo(Player player) {
		return GetPlayerInfo(player.getName());
	}
}
