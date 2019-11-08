package com.firstlinecode.basalt.xeps.ibr;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stream.Feature;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;

@ProtocolObject(namespace="http://jabber.org/features/iq-register", localName="register")
public class Register implements Feature {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/features/iq-register", "register");
}
