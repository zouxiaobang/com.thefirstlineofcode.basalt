package com.firstlinecode.basalt.oxm.parsers.error;

import java.util.List;

import com.firstlinecode.basalt.protocol.core.IError;
import com.firstlinecode.basalt.protocol.core.LangText;
import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stanza.error.BadRequest;
import com.firstlinecode.basalt.protocol.core.stanza.error.StanzaError;
import com.firstlinecode.basalt.protocol.core.stream.error.StreamError;
import com.firstlinecode.basalt.oxm.Attribute;
import com.firstlinecode.basalt.oxm.Value;
import com.firstlinecode.basalt.oxm.parsing.ElementParserAdaptor;
import com.firstlinecode.basalt.oxm.parsing.IElementParser;
import com.firstlinecode.basalt.oxm.parsing.IParser;
import com.firstlinecode.basalt.oxm.parsing.IParserFactory;
import com.firstlinecode.basalt.oxm.parsing.IParsingContext;
import com.firstlinecode.basalt.oxm.parsing.IParsingPath;
import com.firstlinecode.basalt.oxm.parsing.ParserAdaptor;

public class ErrorParserFactory<T extends IError> implements IParserFactory<T> {
	private IError.Type type;
	private IParser<T> parser;
	
	public ErrorParserFactory(IError.Type type) {
		this.type = type;
		parser = new ErrorParser<>(type);
	}

	@Override
	public Protocol getProtocol() {
		if (type == IError.Type.STANZA) {
			return StanzaError.PROTOCOL;
		} else {
			return StreamError.PROTOCOL;
		}
	}
	
	@Override
	public IParser<T> create() {
		return parser;
	}
	
	private class ErrorParser<V extends IError> extends ParserAdaptor<V> {
		private static final String PROTOCOL_TEXT_LOCAL_NAME = "text";
		private static final String PATH_ROOT = "/";
		private static final String STRING_TYPE = "type";
		
		private IError.Type type;
		
		private RootElementParser rootElementParser;
		
		public ErrorParser(IError.Type type) {
			super(null);
			
			this.type = type;
			rootElementParser = new RootElementParser(type);
		}

		@SuppressWarnings("unchecked")
		@Override
		public V createObject() {
			if (type == IError.Type.STANZA) {
				return (V)new StanzaError();
			} else {
				return (V)new StreamError();
			}
		}

		@Override
		public IElementParser<V> getElementParser(IParsingPath parsingPath) {
			if (parsingPath.match(PATH_ROOT)) {
				return rootElementParser;
			}
			
			return null;
		}
		
		@Override
		public void processEmbeddedObject(IParsingContext<V> context, Protocol protocol, Object embedded) {
			if (StreamError.NAMESPACE_STREAM_ERROR_CONTEXT.equals(protocol.getNamespace()) ||
					StanzaError.NAMESPACE_STANZA_ERROR_CONTEXT.equals(protocol.getNamespace())) {
				if (PROTOCOL_TEXT_LOCAL_NAME.equals(protocol.getLocalName())) {
					if (context.getObject().getText() != null) {
						throw new ProtocolException(new BadRequest("Reduplicated element 'text'."));
					}
					
					if (embedded instanceof LangText) {
						context.getObject().setText((LangText)embedded);
					}
				} else {
					if (context.getObject().getDefinedCondition() != null) {
						throw new ProtocolException(new BadRequest("Reduplicated element 'defined-condition'."));
					}
					
					context.getObject().setDefinedCondition((String)embedded);
				}
			} else {
				if (context.getObject().getApplicationSpecificCondition() != null) {
					throw new ProtocolException(new BadRequest("Reduplicated element 'application-specific-condition'."));
				}
				
				context.getObject().setApplicationSpecificCondition(embedded);
			}
		}
		
		private class RootElementParser extends ElementParserAdaptor<V> {
			private IError.Type type;
			
			public RootElementParser(IError.Type type) {
				this.type = type;
			}
			
			@Override
			public void processAttributes(IParsingContext<V> context, List<Attribute> attributes) {
				if (type == IError.Type.STANZA) {
					if (attributes.size() != 1) {
						throw new ProtocolException(new BadRequest("Element 'error' must has and only has an attribute 'type'."));
					}
				
					Attribute attribute = attributes.get(0);
					if (!STRING_TYPE.equals(attribute.getName())) {
						throw new ProtocolException(new BadRequest("Element 'error' must has an attribute 'type'."));
					}
				
					if (attribute.getValue().getType() != Value.Type.STRING)
						throw new ProtocolException(new BadRequest("'type' attribute of 'error' element must be a string."));
					
					String errorType = attribute.getValue().toString();
					StanzaError error = (StanzaError)context.getObject();
					if ("cancel".equals(errorType)) {
						error.setType(StanzaError.Type.CANCEL);
					} else if ("continue".equals(errorType)) {
						error.setType(StanzaError.Type.CONTINUE);
					} else if ("modify".equals(errorType)) {
						error.setType(StanzaError.Type.MODIFY);
					} else if ("auth".equals(errorType)) {
						error.setType(StanzaError.Type.AUTH);
					} else if ("wait".equals(errorType)) {
						error.setType(StanzaError.Type.WAIT);
					} else {
						throw new ProtocolException(new BadRequest(String.format("Invalid 'error-type': %s.",
								attribute.getValue())));
					}
				}
			}
		}
		
	}

}
