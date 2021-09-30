package com.thefirstlineofcode.basalt.protocol.core.stanza.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class UndefinedCondition extends StanzaError {
	public static final String DEFINED_CONDITION = "undefined-condition";
	
	public UndefinedCondition(StanzaError.Type type) {
		this(type, null);
	}
	
	public UndefinedCondition(StanzaError.Type type, String text) {
		this(type, text, null);
	}
	
	public UndefinedCondition(StanzaError.Type type, String text, String lang) {
		super(StanzaError.Type.CANCEL, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
		this.type = type;
		if (this.type == null) {
			type = StanzaError.Type.CANCEL;
		}
	}
}
