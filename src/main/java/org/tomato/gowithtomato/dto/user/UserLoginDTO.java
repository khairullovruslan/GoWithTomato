package org.tomato.gowithtomato.dto.user;

import lombok.Builder;

@Builder
public record UserLoginDTO(
        String login,
        String password) {
}
