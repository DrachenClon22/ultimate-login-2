package com.drachenclon.dreg.HashManager;

import java.security.NoSuchAlgorithmException;

import com.drachenclon.dreg.ByteManager.ByteMath;

public final class HashBuilder {
	private static IHashable _hashBuilder;
	
	public static void init(IHashable hashBuilder) {
		_hashBuilder = hashBuilder;
	}
	
	public static String GetStringHash(String input) {
		try {
			return ByteMath.ByteToString(_hashBuilder.GetHash(input));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] GetByteHash(String input) {
		return GetStringHash(input).getBytes();
	}
	
	public static int GetBlockSize() {
		return _hashBuilder.GetBlockSize();
	}
	
	public static int GetByteSize() {
		return _hashBuilder.GetByteSize();
	}
}
