package com.thefirstlineofcode.basalt.protocol.core.stanza.error;

import com.thefirstlineofcode.basalt.protocol.core.LangText;

public class ItemNotFound extends StanzaError {
	public static final String DEFINED_CONDITION = "item-not-found";
	
	public ItemNotFound() {
		this(null);
	}
	
	public ItemNotFound(String text) {
		this(text, null);
	}
	
	public ItemNotFound(String text, String lang) {
		super(StanzaError.Type.CANCEL, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
