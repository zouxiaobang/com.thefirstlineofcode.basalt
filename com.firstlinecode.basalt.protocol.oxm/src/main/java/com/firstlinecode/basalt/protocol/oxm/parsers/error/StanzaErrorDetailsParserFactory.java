package com.firstlinecode.basalt.protocol.oxm.parsers.error;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stanza.error.StanzaError;

public class StanzaErrorDetailsParserFactory extends ErrorDetailsParserFactory {

	@Override
	public Protocol getProtocol() {
		return StanzaError.PROTOCOL;
	}

}
