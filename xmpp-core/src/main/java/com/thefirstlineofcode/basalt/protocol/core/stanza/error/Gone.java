package com.thefirstlineofcode.basalt.protocol.core.stanza.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class Gone extends StanzaError {
public static final String DEFINED_CONDITION = "gone";
	
	public Gone() {
		this(null);
	}
	
	public Gone(String text) {
		this(text, null);
	}
	
	public Gone(String text, String lang) {
		super(StanzaError.Type.MODIFY, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
