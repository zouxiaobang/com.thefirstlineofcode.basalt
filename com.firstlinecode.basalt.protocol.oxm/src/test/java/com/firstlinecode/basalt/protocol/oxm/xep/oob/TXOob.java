package com.firstlinecode.basalt.protocol.oxm.xep.oob;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.TextOnly;
import com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations.CustomValidator;
import com.firstlinecode.basalt.protocol.oxm.conversion.validators.TUrlValidator;

@ProtocolObject(namespace="jabber:x:oob", localName="x")
public class TXOob {
	public static final Protocol PROTOCOL = new Protocol("jabber:x:oob", "x");
	
	@CustomValidator(TUrlValidator.class)
	@TextOnly
	private String url;
	
	@TextOnly
	private String desc;
	
	public TXOob() {}
	
	public TXOob(String url) {
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
