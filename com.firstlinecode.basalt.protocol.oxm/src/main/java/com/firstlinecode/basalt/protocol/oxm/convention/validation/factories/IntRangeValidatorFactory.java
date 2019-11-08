package com.firstlinecode.basalt.protocol.oxm.convention.validation.factories;

import com.firstlinecode.basalt.protocol.oxm.convention.validation.IValidatorFactory;
import com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations.IntRange;
import com.firstlinecode.basalt.protocol.oxm.validation.IValidator;
import com.firstlinecode.basalt.protocol.oxm.validation.validators.IntRangeValidator;

public class IntRangeValidatorFactory implements IValidatorFactory<IntRange> {

	@Override
	public IValidator<Integer> create(IntRange intRange) {
		return new IntRangeValidator(intRange.min(), intRange.max());
	}

}
