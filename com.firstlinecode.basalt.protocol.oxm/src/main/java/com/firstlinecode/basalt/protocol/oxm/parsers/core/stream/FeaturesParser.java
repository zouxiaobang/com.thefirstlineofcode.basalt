package com.firstlinecode.basalt.protocol.oxm.parsers.core.stream;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stream.Feature;
import com.firstlinecode.basalt.protocol.core.stream.Features;
import com.firstlinecode.basalt.protocol.core.stream.error.InvalidXml;
import com.firstlinecode.basalt.protocol.oxm.annotations.Parser;
import com.firstlinecode.basalt.protocol.oxm.annotations.ProcessEmbeddedObject;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingContext;

@Parser(localName="stream:features", objectType=Features.class)
public class FeaturesParser {
	@ProcessEmbeddedObject
	public void processEmbeddedObject(IParsingContext<Features> context, Protocol protocol, Object embedded) {
		if (!(embedded instanceof Feature)) {
			throw new ProtocolException(new InvalidXml("Only feature object is allowed here."));
		}
		
		context.getObject().getFeatures().add((Feature)embedded);
	}
}
