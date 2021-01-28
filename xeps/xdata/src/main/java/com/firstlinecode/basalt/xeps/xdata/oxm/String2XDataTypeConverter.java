package com.firstlinecode.basalt.xeps.xdata.oxm;

import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stanza.error.BadRequest;
import com.firstlinecode.basalt.oxm.conversion.ConversionException;
import com.firstlinecode.basalt.oxm.conversion.IConverter;
import com.firstlinecode.basalt.xeps.xdata.XData;
import com.firstlinecode.basalt.xeps.xdata.XData.Type;

public class String2XDataTypeConverter implements IConverter<String, XData.Type> {

	@Override
	public Type from(String obj) throws ConversionException {
		try {
			return XData.Type.valueOf(obj.toUpperCase());
		} catch (Exception e) {
			throw new ProtocolException(new BadRequest(), e);
		}
	}

	@Override
	public String to(Type obj) throws ConversionException {
		return obj.toString();
	}

}
