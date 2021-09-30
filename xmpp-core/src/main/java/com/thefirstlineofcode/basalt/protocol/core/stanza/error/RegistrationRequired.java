package com.thefirstlineofcode.basalt.protocol.core.stanza.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class RegistrationRequired extends StanzaError {
	public static final String DEFINED_CONDITION = "registration-required";
	
	public RegistrationRequired() {
		this(null);
	}
	
	public RegistrationRequired(String text) {
		this(text, null);
	}
	
	public RegistrationRequired(String text, String lang) {
		super(StanzaError.Type.AUTH, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
