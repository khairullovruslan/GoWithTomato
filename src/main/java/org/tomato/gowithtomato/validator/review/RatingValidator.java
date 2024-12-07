package org.tomato.gowithtomato.validator.review;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.validator.annotations.ValidEmail;
import org.tomato.gowithtomato.validator.annotations.ValidRating;

public class RatingValidator implements ConstraintValidator<ValidRating, Integer> {

    @Override
    public boolean isValid(Integer rating, ConstraintValidatorContext constraintValidatorContext) {
        if (rating < 1 || rating > 5){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Рейтинг должен иметь значение  не менее 1 и не более 5.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}