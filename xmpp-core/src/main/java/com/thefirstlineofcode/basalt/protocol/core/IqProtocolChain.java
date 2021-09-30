package com.thefirstlineofcode.basalt.protocol.core;

import com.thefirstlineofcode.basalt.protocol.core.stanza.Iq;

public class IqProtocolChain extends ProtocolChain {
	public IqProtocolChain() {
		append(Iq.PROTOCOL);
	}
	
	public IqProtocolChain(Protocol xep) {
		append(Iq.PROTOCOL).append(xep);
	}
}
