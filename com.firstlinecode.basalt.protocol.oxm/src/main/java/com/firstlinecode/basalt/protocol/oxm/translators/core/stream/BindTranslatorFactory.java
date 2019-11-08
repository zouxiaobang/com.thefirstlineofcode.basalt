package com.firstlinecode.basalt.protocol.oxm.translators.core.stream;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stream.Bind;
import com.firstlinecode.basalt.protocol.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslator;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatorFactory;

public class BindTranslatorFactory implements ITranslatorFactory<Bind> {
	private static final ITranslator<Bind> translator = new BindTranslator();

	@Override
	public Class<Bind> getType() {
		return Bind.class;
	}

	@Override
	public ITranslator<Bind> create() {
		return translator;
	}
	
	private static class BindTranslator implements ITranslator<Bind> {

		@Override
		public Protocol getProtocol() {
			return Bind.PROTOCOL;
		}

		@Override
		public String translate(Bind bind, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Bind.PROTOCOL);
			
			if (bind.getJid() != null) {
				writer.writeTextOnly("jid", bind.getJid().toString());
			}
			
			if (bind.getResource() != null) {
				writer.writeTextOnly("resource", bind.getResource());
			}
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
