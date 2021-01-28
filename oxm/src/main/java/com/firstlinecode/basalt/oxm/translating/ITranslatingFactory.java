package com.firstlinecode.basalt.oxm.translating;

import com.firstlinecode.basalt.oxm.IProtocolWriterFactory;

public interface ITranslatingFactory {
	String translate(Object object);
	
	void register(Class<?> type, ITranslatorFactory<?> translatorFactory);
	void unregister(Class<?> type);
	
	IProtocolWriterFactory getWriterFactory();
}
