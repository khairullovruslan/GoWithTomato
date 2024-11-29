package org.tomato.gowithtomato.validator.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.tomato.gowithtomato.validator.EmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String message() default "Email не соответствует требованиям.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
