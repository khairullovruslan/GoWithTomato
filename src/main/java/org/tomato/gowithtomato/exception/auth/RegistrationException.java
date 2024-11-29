package org.tomato.gowithtomato.exception.auth;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.tomato.gowithtomato.dto.UserRegistrationDto;

import java.util.Set;

@Getter
public class RegistrationException extends RuntimeException {
    private final Set<ConstraintViolation<UserRegistrationDto>> violations;

    public RegistrationException(Set<ConstraintViolation<UserRegistrationDto>> violations) {
        this.violations = violations;
    }
}