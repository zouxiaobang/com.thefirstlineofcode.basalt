package com.firstlinecode.basalt.oxm.xml;

import com.firstlinecode.basalt.oxm.IProtocolWriterFactory;
import com.firstlinecode.basalt.oxm.translating.IProtocolWriter;

public class XmlProtocolWriterFactory implements IProtocolWriterFactory {

	@Override
	public IProtocolWriter create() {
		return new XmlProtocolWriter();
	}

}
