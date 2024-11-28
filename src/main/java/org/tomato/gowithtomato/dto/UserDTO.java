package org.tomato.gowithtomato.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.tomato.gowithtomato.validator.annotations.ValidLogin;
import org.tomato.gowithtomato.validator.annotations.ValidPassword;
import org.tomato.gowithtomato.validator.annotations.ValidPhoneNumber;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{
    private Long id;
    @ValidLogin
    private String login;
    @ValidPassword
    private String password;
    @Email
    private String email;
    @ValidPhoneNumber
    private String phoneNumber;
}
