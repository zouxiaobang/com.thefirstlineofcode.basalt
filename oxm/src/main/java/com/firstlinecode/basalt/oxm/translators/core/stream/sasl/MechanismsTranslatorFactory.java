package com.firstlinecode.basalt.oxm.translators.core.stream.sasl;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stream.sasl.Mechanisms;
import com.firstlinecode.basalt.oxm.Value;
import com.firstlinecode.basalt.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.oxm.translating.ITranslator;
import com.firstlinecode.basalt.oxm.translating.ITranslatorFactory;

public class MechanismsTranslatorFactory implements ITranslatorFactory<Mechanisms> {
	private static final ITranslator<Mechanisms> translator = new MechanismsTranslator();

	@Override
	public Class<Mechanisms> getType() {
		return Mechanisms.class;
	}

	@Override
	public ITranslator<Mechanisms> create() {
		return translator;
	}
	
	private static class MechanismsTranslator implements ITranslator<Mechanisms> {

		@Override
		public Protocol getProtocol() {
			return Mechanisms.PROTOCOL;
		}

		@Override
		public String translate(Mechanisms mechanisms, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Mechanisms.PROTOCOL);
			
			for (String mechanism : mechanisms.getMechanisms()) {
				writer.writeTextOnly("mechanism", Value.create(mechanism));
			}
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
