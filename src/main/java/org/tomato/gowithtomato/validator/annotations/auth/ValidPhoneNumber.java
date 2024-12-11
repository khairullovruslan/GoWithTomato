package org.tomato.gowithtomato.validator.annotations.auth;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.tomato.gowithtomato.validator.auth.PhoneNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Неверный формат телефонного номера.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}