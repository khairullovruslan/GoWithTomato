package org.tomato.gowithtomato.exception.auth;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.tomato.gowithtomato.dto.UserDTO;

import java.util.Set;

@Getter
public class RegistrationException extends RuntimeException {
    private final Set<ConstraintViolation<UserDTO>> violations;

    public RegistrationException(Set<ConstraintViolation<UserDTO>> violations) {
        this.violations = violations;
    }
}