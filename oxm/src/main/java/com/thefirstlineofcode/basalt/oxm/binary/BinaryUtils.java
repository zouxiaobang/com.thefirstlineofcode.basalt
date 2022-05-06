package com.thefirstlineofcode.basalt.oxm.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class BinaryUtils {

	
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
		if (!hexString.startsWith(Constants.PREFIX_HEX_NUMBER)) {
			throw new IllegalArgumentException("Invalid HEX string: " + hexString);
		}
		
		String[] bytesString = hexString.split(Constants.PREFIX_HEX_NUMBER);
		byte[] bytes = new byte[bytesString.length - 1];
		for (int i = 1; i < bytesString.length; i++) {
			int num;
			try {
				num = Integer.decode(Constants.PREFIX_HEX_NUMBER + bytesString[i]);
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
	
	public static String encodeToBase64(byte[] data) {
		if (!isBase64Decoded(data))
			throw new IllegalArgumentException(String.format("Not a Base64 decoded bytes. Bytes: %s.",
					getHexStringFromBytes(data)));
		
		data = Arrays.copyOfRange(data, 1, data.length);
		return String.format("%s%s%s",
				Constants.PREFIX_STRING_BASE64_ENCODED,
				Base64.encodeToString(data, false),
				Constants.POSTFIX_STRING_OF_BASE64_ENCODED
			);
	}
	
	public static boolean isBase64Encoded(String content) {
		return content.length() > 4 &&
				content.startsWith(Constants.PREFIX_STRING_BASE64_ENCODED) &&
				content.endsWith(Constants.POSTFIX_STRING_OF_BASE64_ENCODED);
	}
	
	public static boolean isBase64Decoded(byte[] bytes) {
		return bytes.length >= 2 &&
				bytes[0] == Constants.FLAG_BASE64_DECODED;
	}
	
	public static byte[] escape(byte[] bytes) {
		if (bytes.length == 1) {
			return new byte[] {Constants.FLAG_NOREPLACE, bytes[0]};
		}
		
		if (bytes.length == 2) {
			return new byte[] {Constants.FLAG_NOREPLACE, bytes[0], bytes[1]};
		}
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == Constants.FLAG_DOC_BEGINNING_END ||
						bytes[i] == Constants.FLAG_UNIT_SPLLITER ||
						bytes[i] == Constants.FLAG_NOREPLACE ||
						bytes[i] == Constants.FLAG_BASE64_DECODED ||
						bytes[i] == Constants.FLAG_ESCAPE) {
					output.write(Constants.FLAG_ESCAPE);
					output.write(bytes[i]);
				} else {
					output.write(bytes[i]);
				}
			}
			
			return output.toByteArray();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static byte[] unescape(byte[] bytes) throws IOException {
		if (bytes.length == 3 && bytes[0] == Constants.FLAG_ESCAPE) {
			return new byte[] {bytes[1], bytes[2]};
		}
		
		if (bytes.length == 2 && bytes[0] == Constants.FLAG_ESCAPE) {
			return new byte[] {bytes[1]};
		}
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == Constants.FLAG_ESCAPE && i < bytes.length && isEscapedByte(bytes[i + 1])) {
					continue;
				}
				
				output.write(bytes[i]);
			}
			
			return output.toByteArray();
		} finally {
			if (output != null)
				output.close();
		}
	}
	
	public static boolean isEscapedByte(byte b) {
		return b == Constants.FLAG_DOC_BEGINNING_END ||
				b == Constants.FLAG_UNIT_SPLLITER ||
				b== Constants.FLAG_NOREPLACE||
				b== Constants.FLAG_BASE64_DECODED||
				b == Constants.FLAG_ESCAPE;
	}
	
	public static byte[] decodeFromBase64(String content) {
		if (!isBase64Encoded(content))
			throw new IllegalArgumentException(String.format("Not a Base64 encoded content. Content: %s.", content));
		
		String encodedPart = content.substring(3, content.length() - 1);
		byte[] decodedPart = BinaryUtils.escape(Base64.decode(encodedPart));
		
		byte[] withDecodedFlag = new byte[decodedPart.length + 1];
		withDecodedFlag[0] = Constants.FLAG_BASE64_DECODED;
		for (int i = 0; i < decodedPart.length; i++) {
			withDecodedFlag[i + 1] = decodedPart[i];
		}
		
		return withDecodedFlag;
	}
	
	public static byte[] getBytesWithBase64DecodedFlag(byte[] bytes) {
		if (bytes.length >= 2 &&
				bytes[0] == Constants.FLAG_BASE64_DECODED)
			return bytes;
		
		byte[] withBase64DecodedFlag = new byte[bytes.length + 1];
		withBase64DecodedFlag[0] = Constants.FLAG_BASE64_DECODED;
		
		for (int i = 0; i < bytes.length; i++) {
			withBase64DecodedFlag[i + 1] = bytes[i];
		}
		
		return withBase64DecodedFlag;
	}
}
