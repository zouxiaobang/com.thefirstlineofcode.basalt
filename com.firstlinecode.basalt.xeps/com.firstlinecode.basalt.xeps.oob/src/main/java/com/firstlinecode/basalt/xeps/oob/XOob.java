package com.firstlinecode.basalt.xeps.oob;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.oxm.convention.annotations.TextOnly;

@ProtocolObject(namespace="jabber:x:oob", localName="x")
public class XOob {
public static final Protocol PROTOCOL = new Protocol("jabber:x:oob", "x");
	
	@TextOnly
	private String url;
	
	@TextOnly
	private String desc;
	
	public XOob() {}
	
	public XOob(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
