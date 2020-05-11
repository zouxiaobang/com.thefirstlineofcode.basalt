package com.firstlinecode.basalt.oxm.binary;

public class BinaryUtils {
	public static String getBytesString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		
		for (byte b : bytes) {
			sb.append(String.format("0x%02x ", b & 0xff));
		}
		
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		return sb.toString();
	}
	
	public static byte[] getBytesFromString(String sBytes) {
		if (!sBytes.startsWith("0x")) {
			throw new IllegalArgumentException("Invalid bytes string: " + sBytes);
		}
		String[] bytesString = sBytes.split("0x");
		
		byte[] bytes = new byte[bytesString.length - 1];
		for (int i = 1; i < bytesString.length; i++) {
			int num;
			try {
				num = Integer.decode("0x" + bytesString[i]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid bytes string: " + sBytes);
			}
			
			if (num < 0 || num > 255) {
				throw new IllegalArgumentException("Invalid bytes string: " + sBytes);
			}
			
			bytes[i - 1] = (byte)num;
		}
		
		return bytes;
	}
}
