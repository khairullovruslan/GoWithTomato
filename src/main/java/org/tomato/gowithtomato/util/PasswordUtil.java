package org.tomato.gowithtomato.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {
    private final static PasswordUtil INSTANCE = new PasswordUtil();
    private PasswordUtil(){
    }

    public static PasswordUtil getInstance() {
        return INSTANCE;
    }

    public String hashPassword(String plainTextPassword) {
        return BCrypt.withDefaults().hashToString(12, plainTextPassword.toCharArray());
    }

    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plainTextPassword.toCharArray(), hashedPassword).verified;
    }
}