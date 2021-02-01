package com.firstlinecode.basalt.protocol.core.stanza.error;

import com.firstlinecode.basalt.protocol.core.IError;
import com.firstlinecode.basalt.protocol.core.LangText;
import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stanza.Iq;
import com.firstlinecode.basalt.protocol.core.stanza.Stanza;
import com.firstlinecode.basalt.protocol.im.stanza.Message;
import com.firstlinecode.basalt.protocol.im.stanza.Presence;

public class StanzaError extends Stanza implements IError {
	public enum Kind {
		PRESENCE,
		MESSAGE,
		IQ
	}
	
	public enum Type {
		CANCEL,
		CONTINUE,
		MODIFY,
		AUTH,
		WAIT
	}
	
	public static final Protocol PROTOCOL = new Protocol("error");
	public static final String NAMESPACE_STANZA_ERROR_CONTEXT = "urn:ietf:params:xml:ns:xmpp-stanzas";
	public static final Protocol PROTOCOL_ERROR_DEFINED_CONDITION = new Protocol(NAMESPACE_STANZA_ERROR_CONTEXT, "*");
	
	protected StanzaError.Type type;
	protected String definedCondition;
	protected LangText text;
	protected Object ApplicationSpecificCondition;
	protected String senderMessage;
	protected Kind kind;
	
	public StanzaError() {
		this(null);
	}
	
	public StanzaError(StanzaError.Type type) {
		this(type, null);
	}
	
	public StanzaError(StanzaError.Type type, String definedCondition) {
		this(type, definedCondition, null);
	}
	
	public StanzaError(StanzaError.Type type, String definedCondition, LangText text) {
		this.type = type;
		this.definedCondition = definedCondition;
		this.text = text;
	}
	
	// stanza errors are recoverable(rfc3920 9.3)
	@Override
	public boolean closeStream() {
		return false;
	}
	
	@Override
	public String getDefinedCondition() {
		return definedCondition;
	}
	
	@Override
	public LangText getText() {
		return text;
	}
	
	@Override
	public Object getApplicationSpecificCondition() {
		return ApplicationSpecificCondition;
	}
	
	@Override
	public void setApplicationSpecificCondition(Object applicationSpecificCondition) {
		this.ApplicationSpecificCondition = applicationSpecificCondition;
	}
	
	public StanzaError.Type getType() {
		return type;
	}
	
	public void setSenderMessage(String senderMessage) {
		this.senderMessage = senderMessage;
	}
	
	public String getSenderMessage() {
		return senderMessage;
	}
	
	public Kind getKind() {
		return kind;
	}
	
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
	public void setType(StanzaError.Type type) {
		this.type = type;
	}

	@Override
	public void setDefinedCondition(String definedCondition) {
		this.definedCondition = definedCondition;
	}

	@Override
	public void setText(LangText text) {
		this.text = text;
	}
	
	public static <T extends StanzaError> T create(Stanza stanza, Class<T> type) {
		return create(stanza, type, null);
	}
	
	public static <T extends StanzaError> T create(Stanza stanza, Class<T> type, String text) {
		return create(stanza, type, text, null);
	}
	
	@SuppressWarnings("deprecation")
	public static <T extends StanzaError> T create(Stanza stanza, Class<T> type, String text, String lang) {
		T error = null;
		try {
			error = type.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Can't initialize stanza error. Stanza error type: %s", type.getName()), e);
		}
		
		error.setId(stanza.getId());
		if (stanza instanceof Iq) {
			error.setKind(StanzaError.Kind.IQ);
		} else if (stanza instanceof Message) {
			error.setKind(StanzaError.Kind.MESSAGE);
		} else if (stanza instanceof Presence) {
			error.setKind(StanzaError.Kind.PRESENCE);
		} else {
			throw new RuntimeException(String.format("Unsupported stanza type. Stanza type: %s", stanza.getClass().getName()));
		}
		
		if (stanza.getFrom() != null) {
			error.setTo(stanza.getFrom());
		}
		
		if (text != null) {
			error.setText(new LangText(text, lang));
		}
		
		return error;
	}
}
