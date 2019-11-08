package com.firstlinecode.basalt.xeps.muc.xconference;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.BindTo;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.convention.conversion.annotations.String2JabberId;

@ProtocolObject(namespace="jabber:x:conference", localName="x")
public class XConference {
	public static final Protocol PROTOCOL = new Protocol("jabber:x:conference", "x");
	
	@BindTo("continue")
	private boolean continuee;
	@String2JabberId
	private JabberId jid;
	private String password;
	private String reason;
	private String thread;
	
	public boolean isContinue() {
		return continuee;
	}
	
	public void setContinue(boolean continuee) {
		this.continuee = continuee;
	}
	
	public JabberId getJid() {
		return jid;
	}
	
	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getThread() {
		return thread;
	}
	
	public void setThread(String thread) {
		this.thread = thread;
	}
	
}
