package com.firstlinecode.basalt.protocol.oxm.parsers.core.stream.tls;

import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stream.error.BadFormat;
import com.firstlinecode.basalt.protocol.core.stream.tls.StartTls;
import com.firstlinecode.basalt.protocol.oxm.Value;
import com.firstlinecode.basalt.protocol.oxm.annotations.Parser;
import com.firstlinecode.basalt.protocol.oxm.annotations.ProcessText;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingContext;

@Parser(namespace="urn:ietf:params:xml:ns:xmpp-tls", localName="starttls", objectType=StartTls.class)
public class StartTlsParser {
	@ProcessText("/required")
	public void processRequired(IParsingContext<StartTls> context, Value<?> text) {
		if (text != null) {
			throw new ProtocolException(new BadFormat("Element 'required' must be an empty element."));
		}
		
		context.getObject().setRequired(true);
	}
}
