package org.tomato.gowithtomato.validator.review;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.validator.annotations.ValidReviewDescription;

public class ReviewDescriptionValidator implements ConstraintValidator<ValidReviewDescription, String> {

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string.length() > 350) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Длина текста не должна превышать 350 символов")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
