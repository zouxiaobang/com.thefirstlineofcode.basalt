package com.thefirstlineofcode.basalt.oxm;

import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.protocol.core.ProtocolChain;
import com.thefirstlineofcode.basalt.protocol.core.ProtocolException;

public class OxmFactory implements IOxmFactory {
	protected IParsingFactory parsingFactory;
	protected ITranslatingFactory translatingFactory;
	
	public OxmFactory(IParsingFactory parsingFactory, ITranslatingFactory translatingFactory) {
		if (parsingFactory == null)
			throw new IllegalArgumentException("Null parsing factory.");
		
		if (translatingFactory == null)
			throw new IllegalArgumentException("Null translating factory.");
		
		this.parsingFactory = parsingFactory;
		this.translatingFactory = translatingFactory;
	}

	@Override
	public Object parse(String message) {
		return parse(message, false);
	}
	
	private ProtocolException newInternalServerErrorException(Exception e, boolean stream) {
		String errorMessage = e.getMessage();
		if (errorMessage == null) {
			errorMessage = e.getClass().getName();
		}
		
		if (stream) {
			return new ProtocolException(new com.thefirstlineofcode.basalt.protocol.core.stream.error.InternalServerError(), e);
		} else {
			return new ProtocolException(new com.thefirstlineofcode.basalt.protocol.core.stanza.error.InternalServerError(), e);
		}
	}

	@Override
	public Object parse(String message, boolean stream) {
		try {
			return parsingFactory.parse(message, stream);
		} catch (ProtocolException e) {
			throw e;
		} catch (RuntimeException e) {
			throw newInternalServerErrorException(e, stream);
		}
	}

	@Override
	public String translate(Object object) {
		return translatingFactory.translate(object);
	}

	@Override
	public void setParsingFactory(IParsingFactory parsingFactory) {
		this.parsingFactory = parsingFactory;
	}

	@Override
	public IParsingFactory getParsingFactory() {
		return parsingFactory;
	}

	@Override
	public void setTranslatingFactory(ITranslatingFactory translatingFactory) {
		this.translatingFactory = translatingFactory;
	}

	@Override
	public ITranslatingFactory getTranslatingFactory() {
		return translatingFactory;
	}

	@Override
	public void register(ProtocolChain chain, IParserFactory<?> parserFactory) {
		if (parserFactory != null) {
			parsingFactory.register(chain, parserFactory);
		}
	}
	
	@Override
	public void unregister(ProtocolChain chain) {
		parsingFactory.unregister(chain);
	}

	@Override
	public void register(Class<?> type, ITranslatorFactory<?> translatorFactory) {
		if (translatorFactory != null) {
			translatingFactory.register(type, translatorFactory);
		}
	}

	@Override
	public void unregister(Class<?> type) {
		translatingFactory.unregister(type);
	}

}
