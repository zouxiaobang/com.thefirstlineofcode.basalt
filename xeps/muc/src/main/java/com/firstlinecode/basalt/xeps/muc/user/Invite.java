package com.firstlinecode.basalt.xeps.muc.user;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.oxm.convention.annotations.TextOnly;
import com.firstlinecode.basalt.oxm.convention.conversion.annotations.String2JabberId;

public class Invite {
	@TextOnly
	private String reason;
	@String2JabberId
	private JabberId from;
	@String2JabberId
	private JabberId to;
	private Continue continuee;
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public JabberId getFrom() {
		return from;
	}
	
	public void setFrom(JabberId from) {
		this.from = from;
	}
	
	public JabberId getTo() {
		return to;
	}
	
	public void setTo(JabberId to) {
		this.to = to;
	}

	public Continue getContinue() {
		return continuee;
	}

	public void setContinue(Continue continuee) {
		this.continuee = continuee;
	}
	
}
