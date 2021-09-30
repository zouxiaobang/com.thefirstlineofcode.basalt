package com.thefirstlineofcode.basalt.leps.im.subscription;

import com.thefirstlineofcode.basalt.oxm.convention.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.convention.annotations.TextOnly;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;

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
