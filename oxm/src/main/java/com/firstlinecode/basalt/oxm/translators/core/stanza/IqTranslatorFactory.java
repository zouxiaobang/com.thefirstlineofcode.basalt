package com.firstlinecode.basalt.oxm.translators.core.stanza;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stanza.Iq;
import com.firstlinecode.basalt.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.oxm.translating.ITranslator;
import com.firstlinecode.basalt.oxm.translating.ITranslatorFactory;

public class IqTranslatorFactory implements ITranslatorFactory<Iq> {
	private static final ITranslator<Iq> translator = new IqTranslator();

	@Override
	public Class<Iq> getType() {
		return Iq.class;
	}

	@Override
	public ITranslator<Iq> create() {
		return translator;
	}
	
	private static class IqTranslator extends StanzaTranslator<Iq> {

		@Override
		public Protocol getProtocol() {
			return Iq.PROTOCOL;
		}

		@Override
		protected String getType(Iq iq) {
			if (iq.getType() == null) {
				throw new IllegalArgumentException("Null iq type.");
			}
			
			return iq.getType().toString().toLowerCase();
		}

		@Override
		protected void translateSpecific(Iq iq, IProtocolWriter writer, ITranslatingFactory translatingFactory) {}
		
	}

}
