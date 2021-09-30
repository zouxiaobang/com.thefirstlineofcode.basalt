package com.thefirstlineofcode.basalt.protocol.core.stream.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class Conflict extends StreamError {
	public static final String DEFINED_CONDITION = "conflict";
	
	public Conflict() {
		this(null);
	}
	
	public Conflict(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
