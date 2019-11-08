package com.firstlinecode.basalt.protocol.oxm.convention.conversion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConverterFactory {
	Class<? extends IConverterFactory> value();
}
