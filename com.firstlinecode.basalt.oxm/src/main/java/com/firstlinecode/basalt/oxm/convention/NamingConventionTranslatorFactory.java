package com.firstlinecode.basalt.oxm.convention;

import com.firstlinecode.basalt.oxm.translating.ITranslator;
import com.firstlinecode.basalt.oxm.translating.ITranslatorFactory;

public class NamingConventionTranslatorFactory<T> implements ITranslatorFactory<T> {
	private Class<T> type;
	private NamingConventionTranslator<T> translator;
	
	public NamingConventionTranslatorFactory(Class<T> type) {
		this.type = type;
		translator = new NamingConventionTranslator<>(type);
	}

	@Override
	public ITranslator<T> create() {
		return translator;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

}
