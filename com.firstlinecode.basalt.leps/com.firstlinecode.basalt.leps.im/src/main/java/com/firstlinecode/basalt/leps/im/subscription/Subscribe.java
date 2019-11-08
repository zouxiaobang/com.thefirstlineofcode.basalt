package com.firstlinecode.basalt.leps.im.subscription;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.TextOnly;

@ProtocolObject(namespace="urn:lep:subscription", localName="subscribe")
public class Subscribe {
	public static final Protocol PROTOCOL = new Protocol("urn:lep:subscription", "subscribe");
	
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
