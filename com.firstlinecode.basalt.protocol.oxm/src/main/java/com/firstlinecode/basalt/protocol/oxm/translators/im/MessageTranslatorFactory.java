package com.firstlinecode.basalt.protocol.oxm.translators.im;

import com.firstlinecode.basalt.protocol.core.LangText;
import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.im.stanza.Message;
import com.firstlinecode.basalt.protocol.oxm.Attributes;
import com.firstlinecode.basalt.protocol.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslator;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatorFactory;
import com.firstlinecode.basalt.protocol.oxm.translators.core.stanza.StanzaTranslator;

public class MessageTranslatorFactory implements ITranslatorFactory<Message> {
	private static final ITranslator<Message> translator = new MessageTranslator();

	@Override
	public Class<Message> getType() {
		return Message.class;
	}

	@Override
	public ITranslator<Message> create() {
		return translator;
	}
	
	private static class MessageTranslator extends StanzaTranslator<Message> {

		@Override
		public Protocol getProtocol() {
			return Message.PROTOCOL;
		}

		@Override
		protected String getType(Message message) {
			if (message.getType() == null)
				return null;
			
			return message.getType().toString().toLowerCase();
		}

		@Override
		protected void translateSpecific(Message message, IProtocolWriter writer,
					ITranslatingFactory translatingFactory) {
			if (message.getSubjects() != null && message.getSubjects().size() > 0) {
				for (LangText subject : message.getSubjects()) {
					writer.writeElementBegin("subject");
					if (subject.getLang() != null) {
						writer.writeAttributes(new Attributes(LangText.PREFIX_LANG_TEXT,
							LangText.LOCAL_NAME_LANG_TEXT, subject.getLang()).get());
					}
					writer.writeText(subject.getText());
					writer.writeElementEnd();
				}
			}
			
			if (message.getBodies() != null && message.getBodies().size() > 0) {
				for (LangText body : message.getBodies()) {
					writer.writeElementBegin("body");
					if (body.getLang() != null) {
						writer.writeAttributes(new Attributes(LangText.PREFIX_LANG_TEXT,
							LangText.LOCAL_NAME_LANG_TEXT, body.getLang()).get());
					}
					writer.writeText(body.getText());
					writer.writeElementEnd();
				}
			}
			
			if (message.getThread() != null) {
				writer.writeElementBegin("thread");
				writer.writeText(message.getThread());
				writer.writeElementEnd();
			}
		}
		
	}

}
