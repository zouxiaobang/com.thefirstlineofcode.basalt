package com.firstlinecode.basalt.oxm.binary;

public interface IBinaryXmppProtocolConverter {
	byte[] toBinary(String message);
	String toXml(byte[] message);
}
