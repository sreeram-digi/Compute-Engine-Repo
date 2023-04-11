package com.portal.validations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = LoginTypeValidator.class)
@Target({ METHOD, CONSTRUCTOR, PARAMETER, FIELD })
@Retention(RUNTIME)
public @interface LoginType {

	String message() default "Type Not fount";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String[] values() default {};
}