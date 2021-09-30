package com.thefirstlineofcode.basalt.protocol.core.stream.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class RestrictedXml extends StreamError {
	public static final String DEFINED_CONDITION = "restricted-xml";
	
	public RestrictedXml() {
		this(null);
	}
	
	public RestrictedXml(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
