package com.github.imdabigboss.easydatapack.api.exceptions;

/**
 * This exception is thrown when a custom dimension exception occurs.
 */
public class CustomDimensionException extends EasyDatapackException {
    public CustomDimensionException(String message) {
        super(message);
    }

    public CustomDimensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
