package com.firstlinecode.basalt.oxm.convention.validation.factories;

import com.firstlinecode.basalt.oxm.convention.validation.IValidatorFactory;
import com.firstlinecode.basalt.oxm.convention.validation.annotations.Email;
import com.firstlinecode.basalt.oxm.validation.IValidator;
import com.firstlinecode.basalt.oxm.validation.validators.EmailValidator;

public class EmailValidatorFactory implements IValidatorFactory<Email> {
	private static final IValidator<String> validator = new EmailValidator();

	@Override
	public IValidator<String> create(Email annotation) {
		return validator;
	}

}
