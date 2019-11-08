package com.firstlinecode.basalt.leps.im.subscription;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;

@ProtocolObject(namespace="urn:lep:subscription", localName="unsubscribe")
public class Unsubscribe {
	public static final Protocol PROTOCOL = new Protocol("urn:lep:subscription", "unsubscribe");	
}
