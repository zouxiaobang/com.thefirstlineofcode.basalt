package com.thefirstlineofcode.basalt.oxm.convention;

import com.thefirstlineofcode.basalt.oxm.convention.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class NamingConventionParserFactory<T> implements IParserFactory<T> {
	private Protocol protocol;
	private NamingConventionParser<T> parser;
	
	public NamingConventionParserFactory(Class<T> objectType) {
		this(getProtocol(objectType), objectType);
	}
	
	private NamingConventionParserFactory(Protocol protocol, Class<T> objectType) {
		this.protocol = protocol;
		parser = new NamingConventionParser<>(objectType);
	}
	
	private static Protocol getProtocol(Class<?> objectType) {
		ProtocolObject protocolObject = objectType.getAnnotation(ProtocolObject.class);
		if (protocolObject == null) {
			throw new IllegalArgumentException("@ProtocolObject not found.");
		}
		
		return new Protocol(protocolObject.namespace(), protocolObject.localName());
	}

	@Override
	public Protocol getProtocol() {
		return protocol;
	}

	@Override
	public IParser<T> create() {
		return parser;
	}
}
