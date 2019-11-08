package com.firstlinecode.basalt.protocol.core.stanza.error;

import com.firstlinecode.basalt.protocol.core.LangText;

public class RemoteServerNotFound extends StanzaError {
	public static final String DEFINED_CONDITION = "remote-server-not-found";
	
	public RemoteServerNotFound() {
		this(null);
	}
	
	public RemoteServerNotFound(String text) {
		this(text, null);
	}
	
	public RemoteServerNotFound(String text, String lang) {
		super(StanzaError.Type.CANCEL, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
