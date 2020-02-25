package com.firstlinecode.basalt.oxm.convention.conversion;

import java.lang.annotation.Annotation;

import com.firstlinecode.basalt.oxm.conversion.IConverter;

public interface IConversionFactory {
	<T extends Annotation> IConverter<?, ?> getConverter(T annotation);
}
