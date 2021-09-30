package com.thefirstlineofcode.basalt.protocol.core;

import com.thefirstlineofcode.basalt.protocol.im.stanza.Presence;

public class PresenceProtocolChain extends ProtocolChain {
	public PresenceProtocolChain() {
		append(Presence.PROTOCOL);
	}
	
	public PresenceProtocolChain(Protocol xep) {
		append(Presence.PROTOCOL).append(xep);
	}
}
