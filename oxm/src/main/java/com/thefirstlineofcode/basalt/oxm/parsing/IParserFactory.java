package com.thefirstlineofcode.basalt.oxm.parsing;

import com.thefirstlineofcode.basalt.protocol.core.Protocol;

public interface IParserFactory<T> {
	Protocol getProtocol();
	IParser<T> create();
}
