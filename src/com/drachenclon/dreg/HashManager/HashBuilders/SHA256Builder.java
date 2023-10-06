package com.drachenclon.dreg.HashManager.HashBuilders;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.drachenclon.dreg.ByteManager.ByteMath;
import com.drachenclon.dreg.HashManager.IHashable;

public final class SHA256Builder implements IHashable {
	@Override
	public byte[] GetHash(String text) throws NoSuchAlgorithmException {
		byte[] result = MessageDigest.getInstance("SHA-256").digest(text.getBytes());
		String oldCoding = Base64.getEncoder().encodeToString(result);
        return oldCoding.getBytes();
    }

	@Override
	public int GetBlockSize() {
		try {
			return ByteMath.ByteToString(GetHash("asdasd")).length();
		} catch (Exception e) {
			return -1;
		}
	}
	
	@Override
	public int GetByteSize() {
		try {
			return MessageDigest.getInstance("SHA-256").digest("asdasd".getBytes()).length;
		} catch (Exception e) {
			return -1;
		}
	}
}
