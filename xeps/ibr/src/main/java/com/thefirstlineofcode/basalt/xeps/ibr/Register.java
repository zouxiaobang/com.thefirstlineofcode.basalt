package com.thefirstlineofcode.basalt.xeps.ibr;

import com.thefirstlineofcode.basalt.oxm.convention.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;
import com.thefirstlineofcode.basalt.protocol.core.stream.Feature;

@ProtocolObject(namespace="http://jabber.org/features/iq-register", localName="register")
public class Register implements Feature {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/features/iq-register", "register");
}
