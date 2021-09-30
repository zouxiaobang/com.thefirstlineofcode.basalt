package com.thefirstlineofcode.basalt.leps.im.subscription;

import com.thefirstlineofcode.basalt.oxm.convention.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.convention.annotations.TextOnly;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;

@ProtocolObject(namespace="urn:leps:subscription", localName="subscribe")
public class Subscribe {
	public static final Protocol PROTOCOL = new Protocol("urn:leps:subscription", "subscribe");
	
	@TextOnly
	private String message;
	
	public Subscribe() {}
	
	public Subscribe(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
