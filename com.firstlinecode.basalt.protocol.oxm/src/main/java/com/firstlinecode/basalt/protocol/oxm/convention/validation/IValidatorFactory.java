package com.firstlinecode.basalt.protocol.oxm.convention.validation;

import java.lang.annotation.Annotation;

import com.firstlinecode.basalt.protocol.oxm.validation.IValidator;

public interface IValidatorFactory<T extends Annotation> {
	IValidator<?> create(T annotation);
}
