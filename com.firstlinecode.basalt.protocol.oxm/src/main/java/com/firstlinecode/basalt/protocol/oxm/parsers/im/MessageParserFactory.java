package com.firstlinecode.basalt.protocol.oxm.parsers.im;

import java.util.List;

import com.firstlinecode.basalt.protocol.HandyUtils;
import com.firstlinecode.basalt.protocol.core.LangText;
import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stanza.error.BadRequest;
import com.firstlinecode.basalt.protocol.im.stanza.Message;
import com.firstlinecode.basalt.protocol.oxm.Attribute;
import com.firstlinecode.basalt.protocol.oxm.Value;
import com.firstlinecode.basalt.protocol.oxm.parsers.core.stanza.StanzaParser;
import com.firstlinecode.basalt.protocol.oxm.parsing.ElementParserAdaptor;
import com.firstlinecode.basalt.protocol.oxm.parsing.IElementParser;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParser;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingContext;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingPath;
import com.firstlinecode.basalt.protocol.oxm.parsing.ParsingUtils;

public class MessageParserFactory implements IParserFactory<Message> {
	private static final String PATH_MESSAGE_ROOT = "/";
	private static final String PATH_MESSAGE_SUBJECT = "/subject";
	private static final String PATH_MESSAGE_BODY = "/body";
	private static final String PATH_MESSAGE_THREAD = "/thread";
	
	private static final Object KEY_LANG_TEXT = new Object();
	
	@Override
	public Protocol getProtocol() {
		return Message.PROTOCOL;
	}

	@Override
	public IParser<Message> create() {
		return new StanzaParser<Message>() {

			@Override
			public Message createObject() {
				return new Message();
			}
			
			@Override
			public IElementParser<Message> getElementParser(IParsingPath parsingPath) {
				if (parsingPath.match(PATH_MESSAGE_ROOT)) {
					return new LangCheckedElementParser(super.getElementParser(parsingPath));
				} else {
					return super.getElementParser(parsingPath);
				}
			}
			
			@Override
			protected IElementParser<Message> doGetElementParser(IParsingPath parsingPath) {
				if (parsingPath.match(PATH_MESSAGE_SUBJECT)) {
					return new IElementParser<Message>() {
						@Override
						public void processText(IParsingContext<Message> context, Value<?> text) {
							LangText langText = context.removeAttribute(KEY_LANG_TEXT);
							if (langText == null) {
								throw new RuntimeException("Null LangText???");
							}
							
							langText.setText(text == null ? "" : text.toString());
						}
						
						@Override
						public void processAttributes(IParsingContext<Message> context, List<Attribute> attributes) {
							LangText langText = new LangText(null, ParsingUtils.processLangTextAttributes(attributes));
							context.getObject().getSubjects().add(langText);
							context.setAttribute(KEY_LANG_TEXT, langText);
						}
					};
				} else if (parsingPath.match(PATH_MESSAGE_BODY)) {
					return new IElementParser<Message>() {
						@Override
						public void processText(IParsingContext<Message> context, Value<?> text) {
							if (HandyUtils.isBlank(text.toString())) {
								throw new ProtocolException(new BadRequest("Body text is null."));
							}
							
							LangText langText = context.removeAttribute(KEY_LANG_TEXT);
							if (langText == null) {
								throw new RuntimeException("Null LangText???");
							}
							
							langText.setText(text.toString());
						}
						
						@Override
						public void processAttributes(IParsingContext<Message> context, List<Attribute> attributes) {
							LangText langText = new LangText(null, ParsingUtils.processLangTextAttributes(attributes));
							context.getObject().getBodies().add(langText);
							context.setAttribute(KEY_LANG_TEXT, langText);
						}
					};
				} else if (parsingPath.match(PATH_MESSAGE_THREAD)) {
					return new ElementParserAdaptor<Message>() {
						@Override
						public void processText(IParsingContext<Message> context, Value<?> text) {
							if (HandyUtils.isBlank(text.toString())) {
								throw new ProtocolException(new BadRequest("Null thread."));
							}
							
							context.getObject().setThread(text.toString());
						}
					};
				} else {
					return null;
				}
			}

			@Override
			protected void processType(IParsingContext<Message> context, String value) {
				try {
					context.getObject().setType(Message.Type.valueOf(value.toUpperCase()));
				} catch (IllegalArgumentException e) {
					throw new ProtocolException(new BadRequest(String.format("Invalid message type '%s'.", value)));
				}
			}
		};
	}
	
	private class LangCheckedElementParser implements IElementParser<Message> {
		private IElementParser<Message> parentElementParser;
		
		public LangCheckedElementParser(IElementParser<Message> parentElementParser) {
			this.parentElementParser = parentElementParser;
		}

		@Override
		public void processText(IParsingContext<Message> context, Value<?> text) {
			if (!checkElementLang(context.getObject().getSubjects())) {
				throw new ProtocolException(new BadRequest("'subject' element must possess a 'xml:lang' attribute."));
			}
			
			if (!checkElementLang(context.getObject().getBodies())) {
				throw new ProtocolException(new BadRequest("'body' element must possess a 'xml:lang' attribute."));
			}
			
			parentElementParser.processText(context, text);
		}

		@Override
		public void processAttributes(IParsingContext<Message> context, List<Attribute> attributes) {
			parentElementParser.processAttributes(context, attributes);
		}
		
	}
	
	private boolean checkElementLang(List<LangText> elements) {
		if (elements.size() <= 1)
			return true;
		
		boolean defaultFound = false;
		for (LangText element : elements) {
			if (element.getLang() == null || "".equals(element.getLang())) {
				if (defaultFound)
					return false;
				
				defaultFound = true;
			}
		}
		
		return true;
	}

}
