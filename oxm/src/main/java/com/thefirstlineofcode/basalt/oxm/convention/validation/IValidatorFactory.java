package com.thefirstlineofcode.basalt.oxm.convention.validation;

import java.lang.annotation.Annotation;

import com.thefirstlineofcode.basalt.oxm.validation.IValidator;

public interface IValidatorFactory<T extends Annotation> {
	IValidator<?> create(T annotation);
}
