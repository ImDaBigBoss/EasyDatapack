package com.github.imdabigboss.easydatapack.api.exceptions;

/**
 * This exception is thrown when a custom map exception occurs.
 */
public class CustomMapException extends EasyDatapackException {
    public CustomMapException(String message) {
        super(message);
    }

    public CustomMapException(String message, Throwable cause) {
        super(message, cause);
    }
}
