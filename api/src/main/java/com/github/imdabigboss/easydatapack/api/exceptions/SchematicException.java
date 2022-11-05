package com.github.imdabigboss.easydatapack.api.exceptions;

/**
 * This exception is thrown when a schematic exception occurs.
 */
public class SchematicException extends EasyDatapackException {
    public SchematicException(String message) {
        super(message);
    }

    public SchematicException(String message, Throwable cause) {
        super(message, cause);
    }
}
