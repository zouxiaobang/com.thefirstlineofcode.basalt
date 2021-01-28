package com.firstlinecode.basalt.oxm.conversion.validators;

import com.firstlinecode.basalt.oxm.validation.IValidator;
import com.firstlinecode.basalt.oxm.validation.ValidationException;

public class TUrlValidator implements IValidator<String> {

	@Override
	public void validate(String url) throws ValidationException {
		org.apache.commons.validator.UrlValidator validator = new org.apache.commons.validator.UrlValidator();
		if (!validator.isValid(url)) {
			throw new ValidationException(String.format("%s isn't a valid url", url));
		}
	}

}
