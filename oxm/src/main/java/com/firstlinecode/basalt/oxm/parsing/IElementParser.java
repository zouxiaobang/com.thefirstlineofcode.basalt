package com.firstlinecode.basalt.oxm.parsing;

import java.util.List;

import com.firstlinecode.basalt.oxm.Attribute;
import com.firstlinecode.basalt.oxm.Value;

public interface IElementParser<T> {
	void processText(IParsingContext<T> context, Value<?> text);
	void processAttributes(IParsingContext<T> context, List<Attribute> attributes);
}
