package com.github.imdabigboss.easydatapack.api.exceptions;

/**
 * This exception is thrown when a custom item exception occurs.
 */
public class CustomEntityException extends EasyDatapackException {
    public CustomEntityException(String message) {
        super(message);
    }

    public CustomEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
