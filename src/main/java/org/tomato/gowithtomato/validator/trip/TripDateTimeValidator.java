package org.tomato.gowithtomato.validator.trip;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.validator.annotations.trip.ValidTripDateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TripDateTimeValidator implements ConstraintValidator<ValidTripDateTime, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        List<String> errorMessages = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthLater = now.plusMonths(6);
        if (localDateTime.isBefore(now)) {
            errorMessages.add("Дата не должна быть раньше текущей");
        }
        if (localDateTime.isAfter(oneMonthLater)) {
            errorMessages.add("Дата не должна позже, чем на полгода текущей");
        }
        if (!errorMessages.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            for (String message : errorMessages) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
            }
            return false;
        }
        return true;

    }
}
