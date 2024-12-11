package org.tomato.gowithtomato.validator.trip;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.validator.annotations.trip.ValidTripPrice;

import java.math.BigDecimal;

public class TripPriceValidator implements ConstraintValidator<ValidTripPrice, BigDecimal> {
    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {
        if (bigDecimal.compareTo(BigDecimal.valueOf(0)) < 0 ){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Цена за билет должна быть неотрицательной")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
