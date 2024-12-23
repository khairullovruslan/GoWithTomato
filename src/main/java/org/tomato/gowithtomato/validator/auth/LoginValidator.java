package org.tomato.gowithtomato.validator.auth;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.exception.auth.UnauthorizedException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.validator.annotations.auth.ValidLogin;

public class LoginValidator implements ConstraintValidator<ValidLogin, String> {
    private final UserService userService;

    public LoginValidator() {
        this.userService = ServiceFactory.getUserService();
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        try {
            userService.findUserByLogin(login);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пользователь с таким логином уже зарегистрирован")
                    .addConstraintViolation();
            return false;
        } catch (UnauthorizedException e) {
            return true;
        }
    }


}