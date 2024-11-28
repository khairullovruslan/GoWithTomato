package org.tomato.gowithtomato.exception.db;

public class NullConnectionException extends RuntimeException{
    public NullConnectionException(String message) {
        super(message);
    }
}
