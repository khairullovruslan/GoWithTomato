package org.tomato.gowithtomato.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.service.UserService;
import org.tomato.gowithtomato.validator.annotations.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        try {
            UserService.getInstance().findByEmail(login);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пользователь с таким email уже зарегистрирован")
                    .addConstraintViolation();
            return false;
        } catch (UserNotFoundException e) {
            return true;
        }
    }


}