package com.firstlinecode.basalt.oxm.parsers;

import java.lang.reflect.Method;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.oxm.TextObject;
import com.firstlinecode.basalt.oxm.Value;
import com.firstlinecode.basalt.oxm.parsing.ElementParserAdaptor;
import com.firstlinecode.basalt.oxm.parsing.IElementParser;
import com.firstlinecode.basalt.oxm.parsing.IParser;
import com.firstlinecode.basalt.oxm.parsing.IParserFactory;
import com.firstlinecode.basalt.oxm.parsing.IParsingContext;
import com.firstlinecode.basalt.oxm.parsing.IParsingPath;
import com.firstlinecode.basalt.oxm.parsing.ParserAdaptor;

public class SimpleObjectParserFactory<T> implements IParserFactory<T> {
	private Protocol protocol;
	private Class<T> type;
	private String setTextMethodName;
	
	public SimpleObjectParserFactory(Protocol protocol, Class<T> type) {
		this(protocol, type, "setText");
	}
	
	public SimpleObjectParserFactory(Protocol protocol, Class<T> type, String setTextMethodName) {
		this.protocol = protocol;
		this.type = type;
		this.setTextMethodName = setTextMethodName;
	}

	@Override
	public Protocol getProtocol() {
		return protocol;
	}

	@Override
	public IParser<T> create() {
		return new SimpleObjectParser<>(type);
	}
	
	private class SimpleObjectParser<K> extends ParserAdaptor<K> {
		private Method setTextMethod;

		public SimpleObjectParser(Class<? extends K> type) {
			super(type);
			
			if (!TextObject.class.isAssignableFrom(type)) {
				try {
					setTextMethod = type.getMethod(setTextMethodName, new Class<?>[] {String.class});
				} catch (Exception e) {
					// ignore
				}
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public K createObject() {
			try {
				return (K)type.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(String.format("Can't instantiate type %s.", type), e);
			}
		}

		@Override
		public IElementParser<K> getElementParser(IParsingPath parsingPath) {
			if (parsingPath.match("/")) {
				return new ElementParserAdaptor<K>() {
					@Override
					public void processText(IParsingContext<K> context, Value<?> text) {
						if (text == null)
							return;
						
						Object object = context.getObject();
						if (object instanceof TextObject) {
							((TextObject)object).setText(text.toString());
						} else {
							if (setTextMethod == null) {
								throw new RuntimeException(String.format("No setText method found. type %s.", type));
							}
							
							try {
								setTextMethod.invoke(object, text.toString());
							} catch (Exception e) {
								throw new RuntimeException("Can't invoke setText method.", e);
							}
						}
					}
				};
			} else {
				return super.getElementParser(parsingPath);
			}
		}

		@Override
		public void processEmbeddedObject(IParsingContext<K> context, Protocol protocol, Object embedded) {
			super.processEmbeddedObject(context, protocol, embedded);
		}
	}
}
