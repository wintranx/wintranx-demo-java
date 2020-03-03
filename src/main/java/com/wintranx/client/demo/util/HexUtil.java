package com.wintranx.client.demo.util;

public class HexUtil {

	/**
	 * 10进制转16进制
	 * 
	 * @param str
	 * @return
	 */
	public static String DecimalToHex(String str) {
		return ByteUtils.toHexAscii(str.getBytes());
	}

	public static byte[] HexToDecimal(String str) {
		return ByteUtils.fromHexAscii(str);
	}
}
