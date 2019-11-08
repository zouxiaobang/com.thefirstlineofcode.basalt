package com.firstlinecode.basalt.protocol.oxm.parsers.core.stream;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.protocol.core.stream.Bind;
import com.firstlinecode.basalt.protocol.oxm.Value;
import com.firstlinecode.basalt.protocol.oxm.annotations.Parser;
import com.firstlinecode.basalt.protocol.oxm.annotations.ProcessText;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingContext;

@Parser(namespace="urn:ietf:params:xml:ns:xmpp-bind", localName="bind", objectType=Bind.class)
public class BindParser {
	@ProcessText("/jid")
	public void processJid(IParsingContext<Bind> context, Value<?> value) {
		context.getObject().setJid(JabberId.parse(value.toString()));
	}
	
	@ProcessText("/resource")
	public void processResource(IParsingContext<Bind> context, Value<?> value) {
		context.getObject().setResource(value.toString());
	}
}
