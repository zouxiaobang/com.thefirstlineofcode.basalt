package com.firstlinecode.basalt.protocol.oxm.conversion.validators;

import com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations.Validate;
import com.firstlinecode.basalt.protocol.oxm.validation.ValidationException;
import com.firstlinecode.basalt.protocol.oxm.xep.xdata.TField;

public class TXDataValidationClass {
	
	@Validate({"/field", "/reported/field", "/item/field"})
	public void validateField(TField field) throws ValidationException {
		if (field.getOptions() != null && field.getOptions().size() > 0) {
			if (field.getType() != TField.Type.LIST_MULTI && field.getType() != TField.Type.LIST_SINGLE)
				throw new ValidationException(String.format("options is only allowed in list_multi or list_single type field," +
						" but this is a %s type field", field.getType()));
		}
	}

}
