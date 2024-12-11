package org.tomato.gowithtomato.dto.user;

import lombok.Builder;
import org.tomato.gowithtomato.validator.annotations.auth.ValidPassword;

@Builder
public record UserPasswordEditDTO(
        String oldPassword,

        @ValidPassword
        String newPassword,
        String repeatPassword) {
}

