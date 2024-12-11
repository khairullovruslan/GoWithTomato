package org.tomato.gowithtomato.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.tomato.gowithtomato.validator.annotations.auth.ValidEmail;
import org.tomato.gowithtomato.validator.annotations.auth.ValidLogin;
import org.tomato.gowithtomato.validator.annotations.auth.ValidPassword;
import org.tomato.gowithtomato.validator.annotations.auth.ValidPhoneNumber;


@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserRegistrationDTO

        (
                @ValidLogin
                @JsonProperty("loginUser")
                String login,
                @ValidPassword
                @JsonProperty("password")
                String password,
                @Email(message = "Email имеет некорректный формат")
                @ValidEmail
                @JsonProperty("email")
                String email,
                @ValidPhoneNumber
                @JsonProperty("phoneNumber")
                String phoneNumber) {
}
