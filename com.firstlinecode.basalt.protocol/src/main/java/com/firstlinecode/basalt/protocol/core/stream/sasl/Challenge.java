package com.firstlinecode.basalt.protocol.core.stream.sasl;

import com.firstlinecode.basalt.protocol.core.Protocol;

public class Challenge {
	public static final Protocol PROTOCOL = new Protocol("urn:ietf:params:xml:ns:xmpp-sasl", "challenge");
	
	private String text;
	
	public Challenge() {}
	
	public Challenge(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
