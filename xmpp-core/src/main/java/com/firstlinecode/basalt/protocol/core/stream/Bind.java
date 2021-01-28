package com.firstlinecode.basalt.protocol.core.stream;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.protocol.core.Protocol;

public class Bind implements Feature {
	public static final Protocol PROTOCOL = new Protocol("urn:ietf:params:xml:ns:xmpp-bind", "bind");
	
	private String resource;
	private JabberId jid;
	
	public Bind() {}
	
	public Bind(String resource) {
		this.resource = resource;
	}
	
	public Bind(JabberId jid) {
		this.jid = jid;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}

	public JabberId getJid() {
		return jid;
	}

	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
}
