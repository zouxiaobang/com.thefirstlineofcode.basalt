package com.firstlinecode.basalt.xeps.xdata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.firstlinecode.basalt.oxm.convention.conversion.Converter;
import com.firstlinecode.basalt.xeps.xdata.oxm.String2XDataTypeConverter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Converter(String2XDataTypeConverter.class)
public @interface String2XDataType {
}
