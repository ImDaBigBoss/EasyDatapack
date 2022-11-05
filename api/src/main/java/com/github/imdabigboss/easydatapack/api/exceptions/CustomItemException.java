package com.github.imdabigboss.easydatapack.api.exceptions;

/**
 * This exception is thrown when a custom item exception occurs.
 */
public class CustomItemException extends EasyDatapackException {
    public CustomItemException(String message) {
        super(message);
    }

    public CustomItemException(String message, Throwable cause) {
        super(message, cause);
    }
}
