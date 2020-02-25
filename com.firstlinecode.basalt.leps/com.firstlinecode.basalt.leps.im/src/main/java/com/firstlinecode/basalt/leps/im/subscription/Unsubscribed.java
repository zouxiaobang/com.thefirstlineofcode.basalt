package com.firstlinecode.basalt.leps.im.subscription;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.TextOnly;

@ProtocolObject(namespace="urn:leps:subscription", localName="unsubscribed")
public class Unsubscribed {
	public static final Protocol PROTOCOL = new Protocol("urn:leps:subscription", "unsubscribed");
	
	@TextOnly
	private String reason;
	
	public Unsubscribed() {}
	
	public Unsubscribed(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
}
