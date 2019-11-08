package com.firstlinecode.basalt.protocol.oxm.convention;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.ProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParser;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParserFactory;

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
