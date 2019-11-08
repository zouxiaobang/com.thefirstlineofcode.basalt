package com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.firstlinecode.basalt.protocol.oxm.convention.validation.Validator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validator
public @interface Email {
}
