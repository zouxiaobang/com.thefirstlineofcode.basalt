package com.thefirstlineofcode.basalt.oxm.convention.validation.factories;

import com.thefirstlineofcode.basalt.oxm.convention.validation.IValidatorFactory;
import com.thefirstlineofcode.basalt.oxm.convention.validation.annotations.Email;
import com.thefirstlineofcode.basalt.oxm.validation.IValidator;
import com.thefirstlineofcode.basalt.oxm.validation.validators.EmailValidator;

public class EmailValidatorFactory implements IValidatorFactory<Email> {
	private static final IValidator<String> validator = new EmailValidator();

	@Override
	public IValidator<String> create(Email annotation) {
		return validator;
	}

}
