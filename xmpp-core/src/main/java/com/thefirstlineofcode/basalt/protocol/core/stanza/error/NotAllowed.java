package com.thefirstlineofcode.basalt.protocol.core.stanza.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class NotAllowed extends StanzaError {
	public static final String DEFINED_CONDITION = "not-allowed";
	
	public NotAllowed() {
		this(null);
	}
	
	public NotAllowed(String text) {
		this(text, null);
	}
	
	public NotAllowed(String text, String lang) {
		super(StanzaError.Type.CANCEL, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
