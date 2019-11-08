package com.firstlinecode.basalt.protocol.core.stream.error;

import com.firstlinecode.basalt.protocol.core.LangText;

public class BadNamespacePrefix extends StreamError {
	public static final String DEFINED_CONDITION = "bad-namespace-prefix";
	
	public BadNamespacePrefix() {
		this(null);
	}
	
	public BadNamespacePrefix(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
