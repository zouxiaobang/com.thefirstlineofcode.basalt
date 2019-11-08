package com.firstlinecode.basalt.xeps.muc.user;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.protocol.oxm.convention.conversion.annotations.String2JabberId;

public class Actor {
	@String2JabberId
	private JabberId jid;
	private String nick;
	
	public JabberId getJid() {
		return jid;
	}
	
	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
}
