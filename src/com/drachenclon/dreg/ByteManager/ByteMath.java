package com.drachenclon.dreg.ByteManager;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Class needed for any sort of byte math that may be needed within working with byte arrays
 */
public final class ByteMath {
	
	/**
	 * Converts byte array to string in UTF-8 coding
	 * @param input byte array (always in UTF-8 coding)
	 * @return String
	 * @apiNote Input byte array always should be only in UTF-8 coding,
	 * other codings will result to unknown returns and may cause bugs.
	 */
	public static String ByteToString(byte[] input) {
    	return new String(input, StandardCharsets.UTF_8);
    }
	
	/**
	 * Concatenates two given byte arrays
	 * @param a first array
	 * @param b second array
	 * @return Result byte array a+b
	 */
	public static byte[] ConcatenateArrays(byte[] a, byte[] b) {
		byte[] result = Arrays.copyOf(a, a.length + b.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
	
	/**
	 * Uses for getting xor value for every byte element to use it later in approx fast array comparison.
	 * @param array input array to be xor'd
	 * @return Int value result of every element of array xor'd
	 */
	public static int GetXor(byte[] array) {
    	int xor = 0;
    	for (byte b : array) {
    		xor ^= b;
    	}
    	return xor;
    }
	
	/**
	 * Fast xor byte array comparison
	 * @param a first array to be compared
	 * @param b second array to be compared
	 * @return True if xor values of arrays equal.
	 * @apiNote Use only for fast comparison, the function <strong>does not</strong> guarantee a complete equality
	 */
	public static boolean XorEquals(byte[] a,  byte[] b) {
		return (GetXor(a) ^ GetXor(b)) == 0;
	}
}
