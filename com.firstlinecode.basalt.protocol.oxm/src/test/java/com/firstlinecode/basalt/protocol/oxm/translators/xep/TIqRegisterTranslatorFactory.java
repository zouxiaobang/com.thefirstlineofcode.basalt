package com.firstlinecode.basalt.protocol.oxm.translators.xep;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatingFactory;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslator;
import com.firstlinecode.basalt.protocol.oxm.translating.ITranslatorFactory;
import com.firstlinecode.basalt.protocol.oxm.xep.ibr.TIqRegister;
import com.firstlinecode.basalt.protocol.oxm.xep.ibr.TRegistrationField;
import com.firstlinecode.basalt.protocol.oxm.xep.ibr.TRegistrationForm;
import com.firstlinecode.basalt.protocol.oxm.xep.ibr.TRemove;

public class TIqRegisterTranslatorFactory implements ITranslatorFactory<TIqRegister> {
	private ITranslator<TIqRegister> translator = new IqRegisterTranslator();

	@Override
	public Class<TIqRegister> getType() {
		return TIqRegister.class;
	}

	@Override
	public ITranslator<TIqRegister> create() {
		return translator;
	}
	
	private class IqRegisterTranslator implements ITranslator<TIqRegister> {

		@Override
		public Protocol getProtocol() {
			return TIqRegister.PROTOCOL;
		}

		@Override
		public String translate(TIqRegister iqRegister, IProtocolWriter writer,
				ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(TIqRegister.PROTOCOL);
			
			Object register = iqRegister.getRegister();
			
			if (register != null) {
				if (register instanceof TRegistrationForm) {
					TRegistrationForm form = (TRegistrationForm)register;
					
					if (form.isRegistered()) {
						writer.writeElementBegin("registered");
						writer.writeElementEnd();
					}
					
					for (TRegistrationField field : form.getFields()) {
						if (field.getValue() == null) {
							writer.writeEmptyElement(field.getName());
						} else {
							writer.writeTextOnly(field.getName(), field.getValue());
						}
					}
				} else if (register instanceof TRemove) {
					writer.writeElementBegin("remove");
					writer.writeElementEnd();
				} else {
					throw new RuntimeException("Unknown register object.");
				}
			}
			
			if (iqRegister.getOob() != null) {
				String oobString = translatingFactory.translate(iqRegister.getOob());
				if (oobString != null)
					writer.writeString(oobString);
			}
			
			if(iqRegister.getXData() != null) {
				String xDataString = translatingFactory.translate(iqRegister.getXData());
				if (xDataString != null)
					writer.writeString(xDataString);
			}
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
