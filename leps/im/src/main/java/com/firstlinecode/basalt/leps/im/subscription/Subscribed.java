package com.firstlinecode.basalt.leps.im.subscription;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.oxm.convention.annotations.ProtocolObject;

@ProtocolObject(namespace="urn:leps:subscription", localName="subscribed")
public class Subscribed {
	public static final Protocol PROTOCOL = new Protocol("urn:leps:subscription", "subscribed");
}
