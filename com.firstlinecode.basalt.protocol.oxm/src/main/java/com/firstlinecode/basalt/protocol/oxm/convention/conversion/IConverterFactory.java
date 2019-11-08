package com.firstlinecode.basalt.protocol.oxm.convention.conversion;

import java.lang.annotation.Annotation;

import com.firstlinecode.basalt.protocol.oxm.conversion.IConverter;

public interface IConverterFactory {
	IConverter<?, ?> create(Annotation annotation);
}
