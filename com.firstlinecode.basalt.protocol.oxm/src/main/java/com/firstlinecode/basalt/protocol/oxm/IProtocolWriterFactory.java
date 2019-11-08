package com.firstlinecode.basalt.protocol.oxm;

import com.firstlinecode.basalt.protocol.oxm.translating.IProtocolWriter;

public interface IProtocolWriterFactory {
	IProtocolWriter create();
}
