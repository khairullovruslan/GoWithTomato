package org.tomato.gowithtomato.validator.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.tomato.gowithtomato.validator.review.RatingValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RatingValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRating {
    String message() default "Рейтинг должен иметь значение  не менее 1 и не более 5.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}