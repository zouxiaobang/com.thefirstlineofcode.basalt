package com.thefirstlineofcode.basalt.protocol.core.stanza.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class Forbidden extends StanzaError {
	public static final String DEFINED_CONDITION = "forbidden";
	
	public Forbidden() {
		this(null);
	}
	
	public Forbidden(String text) {
		this(text, null);
	}
	
	public Forbidden(String text, String lang) {
		super(StanzaError.Type.AUTH, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
