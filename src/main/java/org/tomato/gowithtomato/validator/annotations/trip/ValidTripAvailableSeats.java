package org.tomato.gowithtomato.validator.annotations.trip;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.tomato.gowithtomato.validator.trip.TripAvailableSeatsValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TripAvailableSeatsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTripAvailableSeats {
    String message() default "Некорректное число свободных мест";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
