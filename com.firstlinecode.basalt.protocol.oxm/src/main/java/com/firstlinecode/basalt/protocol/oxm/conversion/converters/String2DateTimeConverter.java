package com.firstlinecode.basalt.protocol.oxm.conversion.converters;

import java.text.ParseException;

import com.firstlinecode.basalt.protocol.datetime.DateTime;
import com.firstlinecode.basalt.protocol.oxm.conversion.ConversionException;
import com.firstlinecode.basalt.protocol.oxm.conversion.IConverter;

public class String2DateTimeConverter implements IConverter<String, DateTime> {

	@Override
	public DateTime from(String string) throws ConversionException {
		try {
			return DateTime.parse(string);
		} catch (ParseException e) {
			throw new ConversionException(String.format("%s isn't a valid datetime.", string), e);
		}
	}

	@Override
	public String to(DateTime dateTime) throws ConversionException {
		return dateTime.toString();
	}

}
