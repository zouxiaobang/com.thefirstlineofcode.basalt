package com.thefirstlineofcode.basalt.oxm.binary;

public class BinaryUtils {
	private static final String PREFIX_HEX_NUMBER = "0x";

	public static String getHexStringFromBytes(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		
		for (byte b : bytes) {
			sb.append(String.format("0x%02x ", b & 0xff));
		}
		
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		return sb.toString();
	}
	
	public static byte[] getBytesFromHexString(String hexString) {
		if (!hexString.startsWith(PREFIX_HEX_NUMBER)) {
			throw new IllegalArgumentException("Invalid HEX string: " + hexString);
		}
		
		String[] bytesString = hexString.split(PREFIX_HEX_NUMBER);
		byte[] bytes = new byte[bytesString.length - 1];
		for (int i = 1; i < bytesString.length; i++) {
			int num;
			try {
				num = Integer.decode(PREFIX_HEX_NUMBER + bytesString[i]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid HEX string: " + hexString);
			}
			
			if (num < 0 || num > 255) {
				throw new IllegalArgumentException("Invalid HEX string: " + hexString);
			}
			
			bytes[i - 1] = (byte)num;
		}
		
		return bytes;
	}
}
