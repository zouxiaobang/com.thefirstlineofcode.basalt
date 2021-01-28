package com.firstlinecode.basalt.oxm.convention.conversion.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.firstlinecode.basalt.oxm.convention.conversion.Converter;
import com.firstlinecode.basalt.oxm.conversion.converters.String2DateConverter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Converter(String2DateConverter.class)
public @interface String2Date {

}
