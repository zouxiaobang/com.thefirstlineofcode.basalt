package com.thefirstlineofcode.basalt.oxm.binary;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public interface IBinaryXmppProtocolConverter {
	byte[] toBinary(String message);
	String toXml(byte[] message);
	Protocol readProtocol(byte[] data);
}
