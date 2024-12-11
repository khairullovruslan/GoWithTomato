package org.tomato.gowithtomato.validator.trip;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.validator.annotations.trip.ValidTripAvailableSeats;

public class TripAvailableSeatsValidator implements ConstraintValidator<ValidTripAvailableSeats, Integer> {
    @Override
    public boolean isValid(Integer count, ConstraintValidatorContext constraintValidatorContext) {
        if (count < 1) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Количество свободных мест должно быть больше 0")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
