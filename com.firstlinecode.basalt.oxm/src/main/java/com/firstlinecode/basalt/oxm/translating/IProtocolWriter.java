package com.firstlinecode.basalt.oxm.translating;

import java.util.List;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.oxm.Attribute;
import com.firstlinecode.basalt.oxm.Value;

public interface IProtocolWriter {
	IProtocolWriter writeString(String string);
	IProtocolWriter writeProtocolBegin(Protocol protocol);
	IProtocolWriter writeProtocolEnd();
	IProtocolWriter writeElementBegin(String prefix, String localName);
	IProtocolWriter writeElementBegin(String localName);
	IProtocolWriter writeElementEnd();
	IProtocolWriter writeEmptyElement(String prefix, String localName);
	IProtocolWriter writeEmptyElement(String localName);
	IProtocolWriter writeAttributes(List<Attribute> attributes);
	IProtocolWriter writeText(Value<?> value);
	IProtocolWriter writeText(String value);
	IProtocolWriter writeTextOnly(String prefix, String localName, Value<?> value);
	IProtocolWriter writeTextOnly(String localName, Value<?> value);
	IProtocolWriter writeTextOnly(String prefix, String localName, String value);
	IProtocolWriter writeTextOnly(String localName, String value);
	String getDocument();
	IProtocolWriter clear();
}
