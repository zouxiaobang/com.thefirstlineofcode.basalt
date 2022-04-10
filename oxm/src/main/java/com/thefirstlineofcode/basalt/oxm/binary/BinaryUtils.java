package com.thefirstlineofcode.basalt.oxm.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class BinaryUtils {
	private static final byte FLAG_DOC_BEGINNING_END = (byte) 0xff;
	private static final byte FLAG_UNIT_SPLLITER = (byte) 0xfe;
	private static final byte FLAG_ESCAPE = (byte)0xfd;
	private static final byte FLAG_NOREPLACE = (byte)0xfc;
	
	private static final String PREFIX_STRING_BASE64_ENCODED = "$B{";
	private static final String POSTFIX_STRING_OF_BASE64_ENCODED = "}";
	
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
	
	public static String encodeToBase64(byte[] data) {
		if (!isBase64Decoded(data))
			throw new IllegalArgumentException(String.format("Not a Base64 decoded bytes. Bytes: %s.",
					getHexStringFromBytes(data)));
		
		data = Arrays.copyOfRange(data, 2, data.length);
		return String.format("%s%s%s",
				PREFIX_STRING_BASE64_ENCODED,
				Base64.encodeToString(data, false),
				POSTFIX_STRING_OF_BASE64_ENCODED
			);
	}
	
	public static boolean isBase64Encoded(String content) {
		return content.length() > 4 &&
				content.startsWith(PREFIX_STRING_BASE64_ENCODED) &&
				content.endsWith(POSTFIX_STRING_OF_BASE64_ENCODED);
	}
	
	public static boolean isBase64Decoded(byte[] bytes) {
		return bytes.length > 2 &&
				bytes[0] == FLAG_NOREPLACE &&
				bytes[1] == FLAG_NOREPLACE;
	}
	
	public static byte[] escape(byte[] bytes) {
		if (bytes.length == 1) {
			return new byte[] {FLAG_NOREPLACE, bytes[0]};
		}
		
		if (bytes.length == 2) {
			return new byte[] {FLAG_NOREPLACE, bytes[0], bytes[1]};
		}
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == FLAG_DOC_BEGINNING_END ||
						bytes[i] == FLAG_UNIT_SPLLITER ||
						bytes[i] == FLAG_NOREPLACE ||
						bytes[i] == FLAG_ESCAPE) {
					output.write(FLAG_ESCAPE);
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
		if (bytes.length == 3 && bytes[0] == FLAG_ESCAPE) {
			return new byte[] {bytes[1], bytes[2]};
		}
		
		if (bytes.length == 2 && bytes[0] == FLAG_ESCAPE) {
			return new byte[] {bytes[1]};
		}
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == FLAG_ESCAPE && i < bytes.length && isEscapedByte(bytes[i + 1])) {
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
		return b == FLAG_DOC_BEGINNING_END ||
				b == FLAG_UNIT_SPLLITER ||
				b== FLAG_NOREPLACE||
				b == FLAG_ESCAPE;
	}
	
	public static byte[] decodeFromBase64(String content) {
		if (!isBase64Encoded(content))
			throw new IllegalArgumentException(String.format("Not a Base64 encoded content. Content: %s.", content));
		
		String encodedPart = content.substring(3, content.length() - 1);
		byte[] decodedPart = BinaryUtils.escape(Base64.decode(encodedPart));
		
		byte[] withDecodeFlag = new byte[decodedPart.length + 2];
		withDecodeFlag[0] = FLAG_NOREPLACE;
		withDecodeFlag[1] = FLAG_NOREPLACE;
		for (int i = 0; i < decodedPart.length; i++) {
			withDecodeFlag[i + 2] = decodedPart[i];
		}
		
		return withDecodeFlag;
	}
	
	public static byte[] getBytesWithBase64DecodedFlag(byte[] bytes) {
		if (bytes.length > 2 &&
				bytes[0] == FLAG_NOREPLACE &&
				bytes[1] == FLAG_NOREPLACE)
			return bytes;
		
		byte[] withBase64DecodedFlag = new byte[bytes.length + 2];
		withBase64DecodedFlag[0] = FLAG_NOREPLACE;
		withBase64DecodedFlag[1] = FLAG_NOREPLACE;
		
		for (int i = 0; i < bytes.length; i++) {
			withBase64DecodedFlag[i + 2] = bytes[i];
		}
		
		return withBase64DecodedFlag;
	}
}
