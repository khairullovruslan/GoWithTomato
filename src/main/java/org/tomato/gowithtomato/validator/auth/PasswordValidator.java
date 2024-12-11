package org.tomato.gowithtomato.validator.auth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tomato.gowithtomato.validator.annotations.auth.ValidPassword;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<String> errorMessages = new ArrayList<>();

        if (password == null) {
            errorMessages.add("Пароль не может быть пустым.");
        } else {
            if (password.length() < 8) {
                errorMessages.add("Пароль должен содержать не менее 8 символов.");
            }
            if (!password.matches(".*[A-Z].*")) {
                errorMessages.add("Пароль должен содержать заглавные буквы (A-Z).");
            }
            if (!password.matches(".*[a-z].*")) {
                errorMessages.add("Пароль должен содержать строчные буквы (a-z).");
            }
            if (!password.matches(".*[0-9].*")) {
                errorMessages.add("Пароль должен содержать цифры (0-9).");
            }
            if (!password.matches(".*[!@#$%^&*().].*")) {
                errorMessages.add("Пароль должен содержать специальные символы - !@#$%^&*().");
            }
        }

        if (!errorMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (String message : errorMessages) {
                context.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
            }
            return false;
        }

        return true;
    }


}