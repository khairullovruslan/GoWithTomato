package org.tomato.gowithtomato.exception;

public class DaoException extends RuntimeException{
    public DaoException() {
        super("Ошибка DAO");
    }
    public DaoException(String message, Throwable e ){
        super(message, e);
    }
    public DaoException(String message) {
        super(message);
    }
}
