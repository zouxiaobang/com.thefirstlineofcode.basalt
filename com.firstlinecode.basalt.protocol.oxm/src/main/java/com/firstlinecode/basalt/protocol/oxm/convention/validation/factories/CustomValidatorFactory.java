package com.firstlinecode.basalt.protocol.oxm.convention.validation.factories;

import java.util.HashMap;
import java.util.Map;

import com.firstlinecode.basalt.protocol.oxm.convention.IArgsAware;
import com.firstlinecode.basalt.protocol.oxm.convention.annotations.Arg;
import com.firstlinecode.basalt.protocol.oxm.convention.validation.IValidatorFactory;
import com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations.CustomValidator;
import com.firstlinecode.basalt.protocol.oxm.validation.IValidator;

public class CustomValidatorFactory implements IValidatorFactory<CustomValidator> {

	@Override
	public IValidator<?> create(CustomValidator annotation) {
		Class<? extends IValidator<?>> validatorClass =  annotation.value();
		Arg[] args = annotation.args();
		
		IValidator<?> validator = null;
		
		try {
			validator = validatorClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Can't create validator instance %s.", validatorClass.getName()));
		}
		
		if (validator instanceof IArgsAware && args.length > 0) {
			((IArgsAware)validator).setArgs(getArgMap(args));
		}
		
		return validator;
	}

	private Map<String, String> getArgMap(Arg[] args) {
		Map<String, String> map = new HashMap<>(args.length);
		for (Arg arg : args) {
			map.put(arg.name(), arg.value());
		}
		
		return map;
	}

}
