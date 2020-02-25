package com.firstlinecode.basalt.oxm.parsers.core.stanza;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stanza.Iq;
import com.firstlinecode.basalt.protocol.core.stanza.error.BadRequest;
import com.firstlinecode.basalt.oxm.parsing.IElementParser;
import com.firstlinecode.basalt.oxm.parsing.IParser;
import com.firstlinecode.basalt.oxm.parsing.IParserFactory;
import com.firstlinecode.basalt.oxm.parsing.IParsingContext;
import com.firstlinecode.basalt.oxm.parsing.IParsingPath;

public class IqParserFactory implements IParserFactory<Iq> {
	@Override
	public Protocol getProtocol() {
		return Iq.PROTOCOL;
	}

	@Override
	public IParser<Iq> create() {
		return new StanzaParser<Iq>() {

			@Override
			public Iq createObject() {
				Iq iq = new Iq(Iq.Type.GET);
				// Iq object sets a random id in it's constructor. The behaviour may cause some bugs in parsing stage.
				// So we should remove the generated id.
				iq.setId(null);
				
				return iq;
			}

			@Override
			protected IElementParser<Iq> doGetElementParser(IParsingPath parsingPath) {
				return null;
			}
			
			@Override
			protected void processType(IParsingContext<Iq> context, String value) {
				try {
					context.getObject().setType(Iq.Type.valueOf(value.toUpperCase()));
				} catch (IllegalArgumentException e) {
					throw new ProtocolException(new BadRequest(String.format("Invalid iq type '%s'.", value)));
				}
			}
			
			@Override
			public void processEmbeddedObject(IParsingContext<Iq> context,
					Protocol protocol, Object embedded) {
				super.processEmbeddedObject(context, protocol, embedded);
			}
		};
	}

}
