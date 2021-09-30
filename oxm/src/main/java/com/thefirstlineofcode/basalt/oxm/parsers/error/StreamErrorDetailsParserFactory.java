package com.thefirstlineofcode.basalt.oxm.parsers.error;

import com.thefirstlineofcode.basalt.protocol.core.Protocol;
import com.thefirstlineofcode.basalt.protocol.core.stream.error.StreamError;

public class StreamErrorDetailsParserFactory extends ErrorDetailsParserFactory {

	@Override
	public Protocol getProtocol() {
		return StreamError.PROTOCOL;
	}

}
