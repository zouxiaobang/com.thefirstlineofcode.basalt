package com.firstlinecode.basalt.oxm.convention.validation.factories;

import com.firstlinecode.basalt.oxm.convention.validation.IValidatorFactory;
import com.firstlinecode.basalt.oxm.convention.validation.annotations.IntRange;
import com.firstlinecode.basalt.oxm.validation.IValidator;
import com.firstlinecode.basalt.oxm.validation.validators.IntRangeValidator;

public class IntRangeValidatorFactory implements IValidatorFactory<IntRange> {

	@Override
	public IValidator<Integer> create(IntRange intRange) {
		return new IntRangeValidator(intRange.min(), intRange.max());
	}

}
