package com.firstlinecode.basalt.protocol.oxm.convention.validation.factories;

import com.firstlinecode.basalt.protocol.oxm.convention.validation.IValidatorFactory;
import com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations.NotNull;
import com.firstlinecode.basalt.protocol.oxm.validation.IValidator;
import com.firstlinecode.basalt.protocol.oxm.validation.validators.NotNullValidator;

public class NotNullValidatorFactory implements IValidatorFactory<NotNull> {

	@Override
	public IValidator<Object> create(NotNull notNull) {
		return new NotNullValidator<>();
	}

}
