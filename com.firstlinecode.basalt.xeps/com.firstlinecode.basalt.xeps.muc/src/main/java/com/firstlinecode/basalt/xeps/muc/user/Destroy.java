package com.firstlinecode.basalt.xeps.muc.user;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.TextOnly;
import com.firstlinecode.basalt.protocol.oxm.convention.conversion.annotations.String2JabberId;

public class Destroy {
	@TextOnly
	private String reason;
	
	@String2JabberId
	private JabberId jid;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public JabberId getJid() {
		return jid;
	}

	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
}
