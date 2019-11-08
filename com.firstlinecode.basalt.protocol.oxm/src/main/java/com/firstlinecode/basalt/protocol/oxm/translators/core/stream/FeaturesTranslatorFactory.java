package com.firstlinecode.basalt.protocol.oxm.translators.core.stream;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stream.Feature;
import com.firstlinecode.basalt.protocol.core.stream.Features;
import com.firstlinecode.basalt.protocol.oxm.parsing.FlawedProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslator;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatorFactory;

public class FeaturesTranslatorFactory implements ITranslatorFactory<Features> {
	private static final ITranslator<Features> translator = new FeaturesTranslator();

	@Override
	public Class<Features> getType() {
		return Features.class;
	}

	@Override
	public ITranslator<Features> create() {
		return translator;
	}
	
	private static class FeaturesTranslator implements ITranslator<Features> {

		@Override
		public Protocol getProtocol() {
			return Features.PROTOCOL;
		}

		@Override
		public String translate(Features features, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Features.PROTOCOL);
			
			for (Feature feature : features.getFeatures()) {
				if (feature instanceof FlawedProtocolObject)
					continue;
				
				writer.writeString(translatingFactory.translate(feature));
			}
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
