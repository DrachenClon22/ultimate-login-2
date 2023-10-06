package com.drachenclon.dreg.SaltManager;

import java.security.SecureRandom;
import java.util.Base64;

import com.drachenclon.dreg.ByteManager.ByteMath;
import com.drachenclon.dreg.HashManager.HashBuilder;

/**
 * Class salt-generator.
 */
public final class SaltBuilder {
	/**
	 * Used for getting random salt byte array with fixed length.
	 * @return Salt byte array with length of 32.
	 */
	public static byte[] GetRandomSaltByte() {
    	SecureRandom secureRandom = new SecureRandom();
    	/*
    	 * Salt array length is the byte size of hash algorithm,
    	 * example: SHA256=32 bytes
    	 */
    	byte[] salt = new byte[HashBuilder.GetByteSize()];
    	secureRandom.nextBytes(salt);
    	String oldCoding = Base64.getEncoder().encodeToString(salt);
        return oldCoding.getBytes();
    }
	
	/**
	 * Same as {@link #GetRandomSaltByte()} but return in string format.
	 * @return String instead of byte array
	 */
	public static String GetRandomSaltString() {
    	return ByteMath.ByteToString(GetRandomSaltByte());
    }
}
