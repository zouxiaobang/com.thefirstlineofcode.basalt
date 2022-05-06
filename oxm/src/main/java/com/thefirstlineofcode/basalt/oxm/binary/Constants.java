package com.thefirstlineofcode.basalt.oxm.binary;

public class Constants {
	public static final byte FLAG_DOC_BEGINNING_END = (byte) 0xff;
	public static final byte FLAG_UNIT_SPLLITER = (byte) 0xfe;
	public static final byte FLAG_ESCAPE = (byte)0xfd;
	public static final byte FLAG_NOREPLACE = (byte)0xfc;
	public static final byte FLAG_BASE64_DECODED = (byte)0xfb;
	
	public static final String PREFIX_STRING_BASE64_ENCODED = "$B{";
	public static final String POSTFIX_STRING_OF_BASE64_ENCODED = "}";
	
	public static final String PREFIX_HEX_NUMBER = "0x";
	public static final String DEFAULT_CHARSET = "UTF-8";
}
