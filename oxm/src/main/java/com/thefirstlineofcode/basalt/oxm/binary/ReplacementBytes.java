package com.thefirstlineofcode.basalt.oxm.binary;

public class ReplacementBytes {
	private byte first;
	private byte second;
	private String provider;
	
	public ReplacementBytes(byte second) {
		this((byte)0xff, second);
	}
	
	public ReplacementBytes(byte first, byte second) {
		this.first = first;
		this.second = second;
		
		int hexFirst = first & 0xff;
		if (hexFirst != 0xff && (hexFirst < 0xf0 || hexFirst > 0xf9)) {
			throw new IllegalArgumentException("Invalid replacement bytes.");
		}
	}
	
	public void setProvider(String provider) {
		if (provider != null) {
			this.provider = provider.intern();
		} else {
			this.provider = null;
		}
	}
	
	public String getProvider() {
		return provider;
	}
	
	public byte getFirst() {
		return first;
	}
	
	public byte getSecond() {
		return second;
	}
	
	@Override
	public int hashCode() {
		if (first == (byte)0xff) {
			return second & 0xff;
		} else {
			return 31 * (first & 0xff) + (second & 0xff);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ReplacementBytes) {
			ReplacementBytes other = (ReplacementBytes)obj;
			
			return other.first == this.first && other.second == this.second;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		if (first == (byte)0xff) {
			return String.format("%s[0x%02x]", ReplacementBytes.class.getSimpleName(), second & 0xff);
		} else {
			return String.format("%s[0x%02x, 0x%02x]", ReplacementBytes.class.getSimpleName(), first & 0xff, second & 0xff);
		}
	}
	
	public static ReplacementBytes parse(String rbString) {
		byte[] bytes = BinaryUtils.getBytesFromHexString(rbString);
		
		if (bytes.length == 1) {
			bytes = new byte[] {(byte)0xff, bytes[0]};
		}
		
		if (bytes.length != 2) {
			throw new ReplacementBytesFormatException("Invalid replacment bytes string: " + rbString);
		}
		
		if (bytes[0] != (byte)0xff && (bytes[0] < 0xf0 || bytes[0] > 0xfa)) {
			throw new ReplacementBytesFormatException("Invalid replacment bytes string: " + rbString);
		}
			
		return new ReplacementBytes(bytes[0], bytes[1]);
	}
	
	public byte[] toBytes() {
		if (first == (byte)0xff)
			return new byte[] {second};
		
		return new byte[] {first, second};
	}
	
	public static boolean isNamespaceReplacementBytes(ReplacementBytes replacementBytes) {
		return isNamespaceFirstByteOfReplacementBytes(replacementBytes.getFirst());
	}
	
	public static boolean isNamespaceFirstByteOfReplacementBytes(byte first) {
		return (first & 0xff) > 0xf0 && (first & 0xff) < 0xfa;
	}
}
