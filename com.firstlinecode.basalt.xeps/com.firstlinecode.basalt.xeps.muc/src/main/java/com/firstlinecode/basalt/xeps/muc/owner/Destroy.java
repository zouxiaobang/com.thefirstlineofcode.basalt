package com.firstlinecode.basalt.xeps.muc.owner;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.oxm.convention.annotations.TextOnly;
import com.firstlinecode.basalt.oxm.convention.conversion.annotations.String2JabberId;

public class Destroy {
	@TextOnly
	private String reason;
	@TextOnly
	private String password;
	
	@String2JabberId
	private JabberId jid;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JabberId getJid() {
		return jid;
	}

	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
}
