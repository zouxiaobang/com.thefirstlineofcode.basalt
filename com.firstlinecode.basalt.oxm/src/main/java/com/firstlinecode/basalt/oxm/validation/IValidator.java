package com.firstlinecode.basalt.oxm.validation;

public interface IValidator<T> {
	void validate(T object) throws ValidationException;
}
