package com.thefirstlineofcode.basalt.protocol.core.stream.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class BadFormat extends StreamError {
	public static final String DEFINED_CONDITION = "bad-format";
	
	public BadFormat() {
		this(null);
	}
	
	public BadFormat(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
