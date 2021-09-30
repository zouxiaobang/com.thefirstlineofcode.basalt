package com.thefirstlineofcode.basalt.leps.im.subscription;

import com.thefirstlineofcode.basalt.oxm.convention.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;

@ProtocolObject(namespace="urn:leps:subscription", localName="unsubscribe")
public class Unsubscribe {
	public static final Protocol PROTOCOL = new Protocol("urn:leps:subscription", "unsubscribe");	
}
