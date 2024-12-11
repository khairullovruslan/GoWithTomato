package org.tomato.gowithtomato.exception.common;

public class GraphHopperApiException extends RuntimeException {

    public GraphHopperApiException(String message) {
        super(message);
    }

    public GraphHopperApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
