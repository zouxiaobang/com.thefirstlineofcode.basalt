package com.firstlinecode.basalt.protocol.oxm.xep.ibb;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.Text;

@ProtocolObject(namespace="http://jabber.org/protocol/ibb", localName="data")
public class TMessageData {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/ibb", "data");
	private int seq;
	private String sid;
	@Text
	private String text;
	
	public int getSeq() {
		return seq;
	}
	
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public String getSid() {
		return sid;
	}
	
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
