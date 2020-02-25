package com.firstlinecode.basalt.oxm;

import com.firstlinecode.basalt.oxm.translating.IProtocolWriter;

public interface IProtocolWriterFactory {
	IProtocolWriter create();
}
