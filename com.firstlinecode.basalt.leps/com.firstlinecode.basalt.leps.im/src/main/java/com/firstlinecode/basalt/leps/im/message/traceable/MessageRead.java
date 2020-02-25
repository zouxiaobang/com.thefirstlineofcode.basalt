package com.firstlinecode.basalt.leps.im.message.traceable;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.datetime.DateTime;
import com.firstlinecode.basalt.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.oxm.convention.conversion.annotations.String2DateTime;
import com.firstlinecode.basalt.oxm.convention.conversion.annotations.String2JabberId;

@ProtocolObject(namespace="urn:leps:traceable", localName="read")
public class MessageRead {
	public static final Protocol PROTOCOL = new Protocol("urn:leps:traceable", "read");
	
	@String2JabberId
	private JabberId from;
	@String2DateTime
	private DateTime stamp;
	
	public MessageRead(JabberId from, DateTime stamp) {
		this.from = from;
		this.stamp = stamp;
	}
	
	public JabberId getFrom() {
		return from;
	}

	public void setFrom(JabberId from) {
		this.from = from;
	}

	public DateTime getStamp() {
		return stamp;
	}

	public void setStamp(DateTime stamp) {
		this.stamp = stamp;
	}
}
