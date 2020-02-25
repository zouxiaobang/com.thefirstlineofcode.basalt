package com.firstlinecode.basalt.oxm.convention.validation;

import java.lang.annotation.Annotation;

import com.firstlinecode.basalt.oxm.validation.IValidator;

public interface IValidatorFactory<T extends Annotation> {
	IValidator<?> create(T annotation);
}
