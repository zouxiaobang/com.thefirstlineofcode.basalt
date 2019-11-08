package com.firstlinecode.basalt.protocol.oxm;

import com.firstlinecode.basalt.protocol.core.IError;
import com.firstlinecode.basalt.protocol.core.ProtocolChain;
import com.firstlinecode.basalt.protocol.core.stanza.Iq;
import com.firstlinecode.basalt.protocol.core.stanza.error.StanzaError;
import com.firstlinecode.basalt.protocol.core.stream.Stream;
import com.firstlinecode.basalt.protocol.core.stream.error.StreamError;
import com.firstlinecode.basalt.protocol.oxm.annotation.AnnotatedParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsers.core.stanza.IqParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsers.core.stream.StreamParser;
import com.firstlinecode.basalt.protocol.oxm.parsers.error.ErrorParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsers.error.StanzaErrorDetailsParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsers.error.StreamErrorDetailsParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingFactory;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.protocol.oxm.translators.core.stanza.IqTranslatorFactory;
import com.firstlinecode.basalt.protocol.oxm.translators.core.stream.StreamTranslatorFactory;
import com.firstlinecode.basalt.protocol.oxm.translators.error.StanzaErrorTranslatorFactory;
import com.firstlinecode.basalt.protocol.oxm.translators.error.StreamErrorTranslatorFactory;

public class MinimumOxmFactory extends StreamOxmFactory {
	public MinimumOxmFactory(IParsingFactory parsingFactory, ITranslatingFactory translatingFactory) {
		super(parsingFactory, translatingFactory);
		registerMinimumProtocols();
	}

	private void registerMinimumProtocols() {
		// register parsers;
		register(ProtocolChain.first(Iq.PROTOCOL), new IqParserFactory());
		register(ProtocolChain.first(Stream.PROTOCOL), new AnnotatedParserFactory<>(StreamParser.class));
		register(StreamError.class, new StreamErrorTranslatorFactory());
		register(ProtocolChain.first(StreamError.PROTOCOL), new ErrorParserFactory<>(IError.Type.STREAM));
		register(ProtocolChain.first(StreamError.PROTOCOL).next(StreamError.PROTOCOL_ERROR_DEFINED_CONDITION),
				new StreamErrorDetailsParserFactory());
		register(ProtocolChain.first(StanzaError.PROTOCOL), new ErrorParserFactory<>(IError.Type.STANZA));
		register(ProtocolChain.first(StanzaError.PROTOCOL).next(StanzaError.PROTOCOL_ERROR_DEFINED_CONDITION),
				new StanzaErrorDetailsParserFactory());
		
		// register translators
		register(Iq.class, new IqTranslatorFactory());
		register(Stream.class, new StreamTranslatorFactory());
		register(StreamError.class, new StreamErrorTranslatorFactory());
		register(StanzaError.class, new StanzaErrorTranslatorFactory());
	}
}
