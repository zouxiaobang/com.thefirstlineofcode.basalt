package com.firstlinecode.basalt.protocol.oxm.xml;

import com.firstlinecode.basalt.protocol.oxm.IProtocolWriterFactory;
import com.firstlinecode.basalt.protocol.oxm.translating.IProtocolWriter;

public class XmlProtocolWriterFactory implements IProtocolWriterFactory {

	@Override
	public IProtocolWriter create() {
		return new XmlProtocolWriter();
	}

}
