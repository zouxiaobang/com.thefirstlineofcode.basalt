package com.firstlinecode.basalt.protocol.oxm.translating;

import com.firstlinecode.basalt.protocol.oxm.IProtocolWriterFactory;

public interface ITranslatingFactory {
	String translate(Object object);
	
	void register(Class<?> type, ITranslatorFactory<?> translatorFactory);
	void unregister(Class<?> type);
	
	IProtocolWriterFactory getWriterFactory();
}
