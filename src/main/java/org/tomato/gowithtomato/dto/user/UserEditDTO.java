package org.tomato.gowithtomato.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.tomato.gowithtomato.validator.annotations.ValidPhoneNumber;

@Builder
public record UserEditDTO(
        String login,
        @Email String email,
        @ValidPhoneNumber
        String phoneNumber) {
}
