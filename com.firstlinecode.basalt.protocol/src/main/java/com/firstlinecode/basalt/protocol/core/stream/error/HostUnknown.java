package com.firstlinecode.basalt.protocol.core.stream.error;

import com.firstlinecode.basalt.protocol.core.LangText;

public class HostUnknown extends StreamError {
	public static final String DEFINED_CONDITION = "host-unknown";
	
	public HostUnknown() {
		this(null);
	}
	
	public HostUnknown(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
