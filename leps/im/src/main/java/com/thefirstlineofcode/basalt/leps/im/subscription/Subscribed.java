package com.thefirstlineofcode.basalt.leps.im.subscription;

import com.thefirstlineofcode.basalt.oxm.convention.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;

@ProtocolObject(namespace="urn:leps:subscription", localName="subscribed")
public class Subscribed {
	public static final Protocol PROTOCOL = new Protocol("urn:leps:subscription", "subscribed");
}
