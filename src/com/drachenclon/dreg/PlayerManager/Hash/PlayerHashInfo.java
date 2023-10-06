package com.drachenclon.dreg.PlayerManager.Hash;

import com.drachenclon.dreg.ByteManager.ByteMath;
import com.drachenclon.dreg.Exceptions.StringCantBeValidatedException;
import com.drachenclon.dreg.HashManager.HashBuilder;
import com.drachenclon.dreg.ValidatorManager.Validator;

public final class PlayerHashInfo {
	private final byte[] _nicknameHash;
	private byte[] _passwordHash;
	private final byte[] _salt;
	private byte[] _ipHash;
	private final int _attempts;
	private int _newAttempts;
	
	private final String _userHash;
	
	public PlayerHashInfo(PlayerHashInfo hash) {
		this._nicknameHash = hash._nicknameHash;
		this._passwordHash = hash._passwordHash;
		this._salt = hash._salt;
		this._ipHash = hash._ipHash;
		this._attempts = hash._attempts;
		this._newAttempts = hash._newAttempts;
		this._userHash = hash._userHash;
	}
	
	public PlayerHashInfo(String input) throws StringCantBeValidatedException {
		if (Validator.TryValidateHash(input)) {
			String[] blocks = new String[5];
			blocks[4] = input.substring(input.length() - 2);
			int attempts = (int)blocks[4].toCharArray()[0];
			int blockSize = HashBuilder.GetBlockSize();
			for (int i = 0; i < blocks.length - 1; i++) {
				blocks[i] = input.substring((i * blockSize), (i + 1) * blockSize);
			}
			_nicknameHash = blocks[0].getBytes();
			_passwordHash = blocks[1].getBytes();
			_salt = blocks[2].getBytes();
			_ipHash = blocks[3].getBytes();
			_attempts = attempts;
			_newAttempts = _attempts;
			
			_userHash = input;
		} else {
			throw new StringCantBeValidatedException(input);
		}
	}
	
	public PlayerHashInfo(byte[] input) throws StringCantBeValidatedException {
		this(ByteMath.ByteToString(input));
	}
	
	public byte[] GetSalt() {
		return _salt;
	}
	
	public byte[] GetNicknameHash() {
		return _nicknameHash;
	}
	
	public byte[] GetPasswordHash() {
		return _passwordHash;
	}
	
	public byte[] GetIPHash() {
		return _ipHash;
	}
	
	public String GetStartedFullHash() {
		return _userHash;
	}
	
	public String GetCurrentHash() {
		return GetNicknameHashAsString() + GetPasswordHashAsString() + GetSaltAsString() 
		+ GetIPHashAsString() + (char)GetCurrentAttempts() + '\0';
	}
	
	public String GetSaltAsString() {
		return new String(GetSalt());
	}
	
	public String GetNicknameHashAsString() {
		return new String(GetNicknameHash());
	}
	
	public String GetPasswordHashAsString() {
		return new String(GetPasswordHash());
	}
	
	public String GetIPHashAsString() {
		return new String(GetIPHash());
	}
	
	public void SetIpHash(String newIp) {
		_ipHash = newIp.getBytes();
	}
	
	public void SetPasswordHash(String password) {
		_passwordHash = password.getBytes();
	}
	
	public void ResetAttempts() {
		_newAttempts = 0;
	}
	
	public void ReduceAttempts() {
		_newAttempts++;
	}
	
	public int GetCurrentAttempts() {
		return _newAttempts;
	}
}
