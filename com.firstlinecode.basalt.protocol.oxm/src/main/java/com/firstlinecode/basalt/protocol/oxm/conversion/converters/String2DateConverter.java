package com.firstlinecode.basalt.protocol.oxm.conversion.converters;

import java.text.ParseException;

import com.firstlinecode.basalt.protocol.datetime.Date;
import com.firstlinecode.basalt.protocol.oxm.conversion.ConversionException;
import com.firstlinecode.basalt.protocol.oxm.conversion.IConverter;

public class String2DateConverter implements IConverter<String, Date> {

	@Override
	public Date from(String string) throws ConversionException {
		try {
			return Date.parse(string);
		} catch (ParseException e) {
			throw new ConversionException(String.format("%s isn't a valid date.", string), e);
		}
	}

	@Override
	public String to(Date date) throws ConversionException {
		return date.toString();
	}

}
