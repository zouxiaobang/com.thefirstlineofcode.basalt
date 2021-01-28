package com.firstlinecode.basalt.oxm.translators.error;

import com.firstlinecode.basalt.protocol.core.LangText;
import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stream.error.StreamError;
import com.firstlinecode.basalt.oxm.Attributes;
import com.firstlinecode.basalt.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.oxm.translating.ITranslator;
import com.firstlinecode.basalt.oxm.translating.ITranslatorFactory;

public class StreamErrorTranslatorFactory implements ITranslatorFactory<StreamError> {
	private ITranslator<StreamError> translator;
	
	public StreamErrorTranslatorFactory() {
		translator = new StreamErrorTranslator();
	}
	
	public Class<StreamError> getType() {
		return StreamError.class;
	}
	
	@Override
	public ITranslator<StreamError> create() {
		return translator;
	}
	
	private class StreamErrorTranslator implements ITranslator<StreamError> {

		@Override
		public Protocol getProtocol() {
			return StreamError.PROTOCOL;
		}

		@Override
		public String translate(StreamError error, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(StreamError.PROTOCOL);
			
			if (error.getDefinedCondition() != null) {
				writer.writeProtocolBegin(new Protocol(StreamError.NAMESPACE_STREAM_ERROR_CONTEXT,
						error.getDefinedCondition()));
				writer.writeProtocolEnd();
			}
			
			if (error.getText() != null) {
				writer.writeProtocolBegin(new Protocol(StreamError.NAMESPACE_STREAM_ERROR_CONTEXT, "text"));
				writer.writeAttributes(new Attributes().
						add(LangText.PREFIX_LANG_TEXT, LangText.LOCAL_NAME_LANG_TEXT, error.getText().getLang()).
						get());
				writer.writeText(error.getText().getText());
				writer.writeProtocolEnd();
			}
			
			if (error.getApplicationSpecificCondition() != null) {
				String applicationSpecificCondition = translatingFactory.translate(
						error.getApplicationSpecificCondition());
				
				if (applicationSpecificCondition != null) {
					writer.writeString(applicationSpecificCondition);
				}
			}
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
	}

}
