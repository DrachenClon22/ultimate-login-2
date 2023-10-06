package com.drachenclon.dreg.HashManager;

import java.security.NoSuchAlgorithmException;

public interface IHashable {
	// Return raw byte hash in UTF-8(!) coding
	public byte[] GetHash(String text) throws NoSuchAlgorithmException;
	// Return string length of block, not byte size
	public int GetBlockSize();
	// Return byte size of algorithm
	public int GetByteSize();
}
