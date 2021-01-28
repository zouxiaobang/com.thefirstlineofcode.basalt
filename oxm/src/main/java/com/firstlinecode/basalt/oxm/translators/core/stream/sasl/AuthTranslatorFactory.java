package com.firstlinecode.basalt.oxm.translators.core.stream.sasl;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stream.sasl.Auth;
import com.firstlinecode.basalt.oxm.Attributes;
import com.firstlinecode.basalt.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.oxm.translating.ITranslator;
import com.firstlinecode.basalt.oxm.translating.ITranslatorFactory;

public class AuthTranslatorFactory implements ITranslatorFactory<Auth> {
	private static final ITranslator<Auth> translator = new AuthTranslator();

	@Override
	public Class<Auth> getType() {
		return Auth.class;
	}

	@Override
	public ITranslator<Auth> create() {
		return translator;
	}
	
	private static class AuthTranslator implements ITranslator<Auth> {

		@Override
		public Protocol getProtocol() {
			return Auth.PROTOCOL;
		}

		@Override
		public String translate(Auth auth, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Auth.PROTOCOL);
			
			writer.writeAttributes(new Attributes().
					add(Auth.ATTRIBUTE_NAME_MECHANISM, auth.getMechanism()).
					get()
				);
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
