package com.firstlinecode.basalt.protocol.oxm.translating;

import com.firstlinecode.basalt.protocol.core.Protocol;

public interface ITranslator<T> {
	Protocol getProtocol();
	String translate(T object, IProtocolWriter writer, ITranslatingFactory translatingFactory);
}
