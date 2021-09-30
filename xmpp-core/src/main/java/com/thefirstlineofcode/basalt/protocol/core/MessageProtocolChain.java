package com.thefirstlineofcode.basalt.protocol.core;

import com.thefirstlineofcode.basalt.protocol.im.stanza.Message;

public class MessageProtocolChain extends ProtocolChain {
	public MessageProtocolChain() {
		append(Message.PROTOCOL);
	}
	
	public MessageProtocolChain(Protocol xep) {
		append(Message.PROTOCOL).append(xep);
	}
}
