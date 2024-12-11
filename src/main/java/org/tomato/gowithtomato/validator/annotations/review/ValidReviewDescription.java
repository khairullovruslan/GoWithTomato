package org.tomato.gowithtomato.validator.annotations.review;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.tomato.gowithtomato.validator.review.ReviewDescriptionValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReviewDescriptionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReviewDescription {
    String message() default "Длина текста не должна превышать 350 символов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
