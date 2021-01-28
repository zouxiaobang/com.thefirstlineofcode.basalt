package com.firstlinecode.basalt.protocol.core.stream.error;

import com.firstlinecode.basalt.protocol.core.LangText;

public class InvalidFrom extends StreamError {
	public static final String DEFINED_CONDITION = "invalid-from";
	
	public InvalidFrom() {
		this(null);
	}
	
	public InvalidFrom(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
