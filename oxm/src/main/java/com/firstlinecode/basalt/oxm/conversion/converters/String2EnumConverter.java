package com.firstlinecode.basalt.oxm.conversion.converters;

import com.firstlinecode.basalt.oxm.conversion.ConversionException;
import com.firstlinecode.basalt.oxm.conversion.IConverter;

public class String2EnumConverter implements IConverter<String, Object> {

	private Class<?> type;
	private boolean upperCase;
	private boolean dashToUnderline;
	
	public String2EnumConverter(Class<?> type, boolean upperCase, boolean dashToUnderline) {
		this.type = type;
		this.upperCase = upperCase;
		this.dashToUnderline = dashToUnderline;
	}
	
	@Override
	public Object from(String obj) throws ConversionException {
		if (type == null) {
			throw new RuntimeException("StringToEnumConverter needs a 'type' argument.");
		}
			
		String enumString = convertToEnumString(obj);
		
		Object[] objects = type.getEnumConstants();
		for (Object object : objects) {
			if (object.toString().equals(enumString))
				return object;
		}
		
		throw new ConversionException(String.format("Can't convert %s to enum %s.", obj, type.getName()));
	}

	private String convertToEnumString(String string) {
		String enumString = new String(string);
		if (upperCase) {
			enumString = enumString.toUpperCase();
		}
		
		if (dashToUnderline) {
			enumString = enumString.replace('-', '_');
		}
		
		return enumString;
	}

	@Override
	public String to(Object obj) throws ConversionException {
		if (!obj.getClass().isEnum()) {
			throw new ConversionException(String.format("Need a enum, but is %s.", obj.getClass()));
		}
		
		for (Object enumConstant : obj.getClass().getEnumConstants()) {
			String enumString = enumConstant.toString();
			if (!enumString.equals(obj.toString()))
				continue;
			
			if (upperCase) {
				enumString = enumString.toLowerCase();
			}
			
			if (dashToUnderline) {
				enumString = enumString.replace('_', '-');
			}
			
			return enumString;
		}
		
		throw new ConversionException("Can't convert enum to string!!!???");
	}

}
