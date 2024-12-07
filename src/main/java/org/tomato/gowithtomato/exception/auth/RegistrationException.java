package org.tomato.gowithtomato.exception.auth;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.tomato.gowithtomato.dto.user.UserRegistrationDTO;

import java.util.Set;

@Getter
public class RegistrationException extends RuntimeException {
    private final Set<ConstraintViolation<UserRegistrationDTO>> violations;

    public RegistrationException(Set<ConstraintViolation<UserRegistrationDTO>> violations) {
        this.violations = violations;
    }
}