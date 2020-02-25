package com.firstlinecode.basalt.oxm.convention.conversion;

import java.lang.annotation.Annotation;

import com.firstlinecode.basalt.oxm.conversion.IConverter;

public interface IConverterFactory {
	IConverter<?, ?> create(Annotation annotation);
}
