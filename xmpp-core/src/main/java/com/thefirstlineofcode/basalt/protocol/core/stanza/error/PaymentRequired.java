package com.thefirstlineofcode.basalt.protocol.core.stanza.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class PaymentRequired extends StanzaError {
	public static final String DEFINED_CONDITION = "payment-required";
	
	public PaymentRequired() {
		this(null);
	}
	
	public PaymentRequired(String text) {
		this(text, null);
	}
	
	public PaymentRequired(String text, String lang) {
		super(StanzaError.Type.AUTH, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
