package org.tomato.gowithtomato.exception.db;

public class UniqueSqlException extends RuntimeException{
    public UniqueSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
