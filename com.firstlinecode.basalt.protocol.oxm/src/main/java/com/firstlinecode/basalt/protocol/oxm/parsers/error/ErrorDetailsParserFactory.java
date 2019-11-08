package com.firstlinecode.basalt.protocol.oxm.parsers.error;

import java.util.List;

import com.firstlinecode.basalt.protocol.core.LangText;
import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.Attribute;
import com.firstlinecode.basalt.protocol.oxm.Value;
import com.firstlinecode.basalt.protocol.oxm.parsing.BadMessageException;
import com.firstlinecode.basalt.protocol.oxm.parsing.ElementParserAdaptor;
import com.firstlinecode.basalt.protocol.oxm.parsing.IElementParser;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParser;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingContext;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingPath;
import com.firstlinecode.basalt.protocol.oxm.parsing.ParserAdaptor;
import com.firstlinecode.basalt.protocol.oxm.parsing.ParsingContext;
import com.firstlinecode.basalt.protocol.oxm.parsing.ParsingUtils;

public abstract class ErrorDetailsParserFactory implements IParserFactory<Object> {
	private static final String PATH_ROOT = "/";
	
	private static final RootElementParser rootElementParser = new RootElementParser();

	@Override
	public IParser<Object> create() {
		return new ErrorDetailsParser();
	}
	
	public class ErrorDetailsParser extends ParserAdaptor<Object> {
		public ErrorDetailsParser() {
			super(null);
		}
		
		@Override
		public Object createObject() {
			return null;
		}
		
		@Override
		public IElementParser<Object> getElementParser(IParsingPath parsingPath) {
			if (parsingPath.match(PATH_ROOT)) {
				return rootElementParser;
			}

			return null;
		} 
		
	}
	
	private static class RootElementParser extends ElementParserAdaptor<Object> {
		private static final String PROTOCOL_TEXT_LOCAL_NAME = "text";
		
		@Override
		public void processAttributes(IParsingContext<Object> context, List<Attribute> attributes) {
			Protocol protocol = context.getProtocolChain().get(context.getProtocolChain().size() - 1);
			
			if (PROTOCOL_TEXT_LOCAL_NAME.equals(protocol.getLocalName())) {
				String lang = ParsingUtils.processLangTextAttributes(attributes);
				((ParsingContext<Object>)context).setObject(new LangText(null, lang));
			} else {
				((ParsingContext<Object>)context).setObject(protocol.getLocalName());
			}
		}
		
		@Override
		public void processText(IParsingContext<Object> context, Value<?> text) {
			Protocol protocol = context.getProtocolChain().get(context.getProtocolChain().size() - 1);
			
			if (PROTOCOL_TEXT_LOCAL_NAME.equals(protocol.getLocalName())) {
				((LangText)(context.getObject())).setText(text.toString());
			} else {
				if (text != null) {
					throw new BadMessageException(String.format("Text not allowed in here. Protocol %s.", context.getProtocolChain()));
				}
			}
		}
	}
}
