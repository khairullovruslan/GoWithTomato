package org.tomato.gowithtomato.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.tomato.gowithtomato.validator.annotations.ValidEmail;
import org.tomato.gowithtomato.validator.annotations.ValidLogin;
import org.tomato.gowithtomato.validator.annotations.ValidPassword;
import org.tomato.gowithtomato.validator.annotations.ValidPhoneNumber;


@Builder
public record UserRegistrationDto(
        @ValidLogin String login,
        @ValidPassword
        String password,
        @Email @ValidEmail String email,
        @ValidPhoneNumber
        String phoneNumber) {
}
