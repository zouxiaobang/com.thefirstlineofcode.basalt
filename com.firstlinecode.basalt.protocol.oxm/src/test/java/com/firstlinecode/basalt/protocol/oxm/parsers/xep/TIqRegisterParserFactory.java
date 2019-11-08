package com.firstlinecode.basalt.protocol.oxm.parsers.xep;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stanza.error.BadRequest;
import com.firstlinecode.basalt.protocol.oxm.Value;
import com.firstlinecode.basalt.protocol.oxm.parsing.ElementParserAdaptor;
import com.firstlinecode.basalt.protocol.oxm.parsing.IElementParser;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParser;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingContext;
import com.firstlinecode.basalt.protocol.oxm.parsing.IParsingPath;
import com.firstlinecode.basalt.protocol.oxm.parsing.ParserAdaptor;
import com.firstlinecode.basalt.protocol.oxm.xep.ibr.TIqRegister;
import com.firstlinecode.basalt.protocol.oxm.xep.ibr.TRegistrationField;
import com.firstlinecode.basalt.protocol.oxm.xep.ibr.TRegistrationForm;
import com.firstlinecode.basalt.protocol.oxm.xep.ibr.TRemove;
import com.firstlinecode.basalt.protocol.oxm.xep.oob.TXOob;
import com.firstlinecode.basalt.protocol.oxm.xep.xdata.TXData;

public class TIqRegisterParserFactory implements IParserFactory<TIqRegister> {
	@Override
	public Protocol getProtocol() {
		return TIqRegister.PROTOCOL;
	}

	@Override
	public IParser<TIqRegister> create() {
		return new IqRegisterParser();
	}
	
	private static class IqRegisterParser extends ParserAdaptor<TIqRegister> {
		private static String ROOT_PARSING_PATH = "/";
		
		private static String[] stringFields = new String[] {
			"instructions",
			"username",
			"nick",
			"password",
			"name",
			"first",
			"last",
			"email",
			"address",
			"city",
			"state",
			"zip",
			"phone",
			"url",
			"date",
			"misc",
			"text",
			"key"
		};
		
		private IElementParser<TIqRegister> stringFieldElementParser = new StringFieldElementParser();

		public IqRegisterParser() {
			super(TIqRegister.class);
		}
		
		@Override
		public void processEmbeddedObject(IParsingContext<TIqRegister> context, Protocol protocol, Object embedded) {
			if (protocol.equals(TXData.PROTOCOL)) {
				context.getObject().setXData((TXData)embedded);
			} else if (protocol.equals(TXOob.PROTOCOL)) {
				context.getObject().setOob((TXOob)embedded);
			} else {
				super.processEmbeddedObject(context, protocol, embedded);
			}
		}
		
		@Override
		public IElementParser<TIqRegister> getElementParser(IParsingPath parsingPath) {
			if (parsingPath.match("/")) {
				return new ElementParserAdaptor<>();
			} else if (parsingPath.match("/registered")) {
				return new ElementParserAdaptor<TIqRegister>() {
					@Override
					public void processText(IParsingContext<TIqRegister> context, Value<?> text) {
						super.processText(context, text);
						
						TRegistrationForm form = getRegistrationForm(context);
						form.setRegistered(true);
					}
				};
			} else if (parsingPath.match("/remove")) {
				return new ElementParserAdaptor<TIqRegister>() {
					@Override
					public void processText(IParsingContext<TIqRegister> context, Value<?> text) {
						super.processText(context, text);
						
						if (context.getObject().getRegister() != null) {
							throw new ProtocolException(new BadRequest("'remove' must be the only element in iq register."));
						}
						
						context.getObject().setRegister(new TRemove());
					}
				};
			} else {
				for (String fieldPath : stringFields) {
					if (parsingPath.match((ROOT_PARSING_PATH + fieldPath))) {
						return stringFieldElementParser;
					}
				}
				
				return super.getElementParser(parsingPath);
			}
		}
		
		private static TRegistrationForm getRegistrationForm(IParsingContext<TIqRegister> context) {
			Object register = context.getObject().getRegister();
			
			if (register == null) {
				TRegistrationForm form = new TRegistrationForm();
				context.getObject().setRegister(form);
				
				return form;
			} else {
				if (!(register instanceof TRegistrationForm)) {
					throw new ProtocolException(new BadRequest("'register' must be a registration form."));
				}
				
				return (TRegistrationForm)register;
			}
		}
		
		private static class StringFieldElementParser extends ElementParserAdaptor<TIqRegister> {
			@Override
			public void processText(IParsingContext<TIqRegister> context, Value<?> text) {
				String parsingPathString = context.getParsingPath().toString();
				String fieldName = parsingPathString.substring(1, parsingPathString.length());
				TRegistrationForm form = getRegistrationForm(context);
				
				for (TRegistrationField field : form.getFields()) {
					if (field.getName().equals(fieldName)) {
						throw new ProtocolException(new BadRequest(String.format(
							"Reduplicated registration field %s", fieldName)));
					}
				}
				
				form.getFields().add(new TRegistrationField(fieldName, text == null ? null : text.toString()));
			}
		}
	}
}
