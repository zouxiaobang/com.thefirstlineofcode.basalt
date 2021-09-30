package com.thefirstlineofcode.basalt.protocol.core.stanza.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class ServiceUnavailable extends StanzaError {
	public static final String DEFINED_CONDITION = "service-unavailable";
	
	public ServiceUnavailable() {
		this(null);
	}
	
	public ServiceUnavailable(String text) {
		this(text, null);
	}
	
	public ServiceUnavailable(String text, String lang) {
		super(StanzaError.Type.CANCEL, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
