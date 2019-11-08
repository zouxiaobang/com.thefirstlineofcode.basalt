package com.firstlinecode.basalt.xeps.muc.owner;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stanza.error.BadRequest;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations.Validate;
import com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations.ValidationClass;
import com.firstlinecode.basalt.xeps.muc.user.Destroy;
import com.firstlinecode.basalt.xeps.xdata.XData;

@ProtocolObject(namespace="http://jabber.org/protocol/muc#owner", localName="query")
@ValidationClass
public class MucOwner {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/muc#owner", "query");
	
	private XData xData;
	private Destroy destroy;
	
	public XData getXData() {
		return xData;
	}
	
	public void setXData(XData xData) {
		this.xData = xData;
	}
	
	public Destroy getDestroy() {
		return destroy;
	}
	
	public void setDestroy(Destroy destroy) {
		this.destroy = destroy;
	}
	
	@Validate("/")
	public void validateMucOwner(MucOwner mucOwner) {
		if (mucOwner.getXData() != null && mucOwner.getDestroy() != null) {
			throw new ProtocolException(new BadRequest("only one child element('xdata' or 'destroy') allowed"));
		}
	}
	
}
