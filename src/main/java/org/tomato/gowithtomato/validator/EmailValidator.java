package org.tomato.gowithtomato.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.factory.ServiceFactory;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.validator.annotations.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private final UserService userService;

    public EmailValidator() {
        this.userService = ServiceFactory.getUserService();
    }


    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        try {
            userService.findByEmail(login);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пользователь с таким email уже зарегистрирован")
                    .addConstraintViolation();
            return false;
        } catch (UserNotFoundException e) {
            return true;
        }
    }


}