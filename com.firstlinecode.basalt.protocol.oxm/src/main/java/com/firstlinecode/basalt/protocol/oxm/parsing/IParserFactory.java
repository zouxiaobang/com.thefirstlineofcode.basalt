package com.firstlinecode.basalt.protocol.oxm.parsing;

import com.firstlinecode.basalt.protocol.core.Protocol;

public interface IParserFactory<T> {
	Protocol getProtocol();
	IParser<T> create();
}
