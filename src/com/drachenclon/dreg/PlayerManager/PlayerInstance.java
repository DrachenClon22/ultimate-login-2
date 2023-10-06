package com.drachenclon.dreg.PlayerManager;

import org.bukkit.entity.Player;

import com.drachenclon.dreg.PlayerManager.Hash.PlayerHashInfo;

public class PlayerInstance {
	private final Player _player;
	private final String _playerIP;
	private PlayerHashInfo _hashInfo;
	
	public PlayerInstance(Player player, PlayerHashInfo hashInfo) {
		_player = player;
		_hashInfo = hashInfo;
		
		if (_player != null) {
			_playerIP = _player.getAddress().getAddress().toString();
		} else {
			_playerIP = "1.1.1.1";
		}
	}
	
	public String GetIP() {
		return _playerIP;
	}
	
	public Player GetPlayer() {
		return _player;
	}
	
	public PlayerHashInfo GetHashInfo() {
		return _hashInfo;
	}
	
	public void SetHashInfo(PlayerHashInfo hash) {
		_hashInfo = hash;
	}
}
