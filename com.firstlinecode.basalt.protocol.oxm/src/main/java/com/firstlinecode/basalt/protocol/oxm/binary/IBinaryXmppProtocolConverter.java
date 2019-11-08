package com.firstlinecode.basalt.protocol.oxm.binary;

public interface IBinaryXmppProtocolConverter {
	byte[] toBinary(String message);
	String toXml(byte[] message);
}
