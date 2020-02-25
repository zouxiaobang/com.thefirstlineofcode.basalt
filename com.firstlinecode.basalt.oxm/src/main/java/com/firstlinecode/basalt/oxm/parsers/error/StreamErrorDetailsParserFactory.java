package com.firstlinecode.basalt.oxm.parsers.error;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stream.error.StreamError;

public class StreamErrorDetailsParserFactory extends ErrorDetailsParserFactory {

	@Override
	public Protocol getProtocol() {
		return StreamError.PROTOCOL;
	}

}
