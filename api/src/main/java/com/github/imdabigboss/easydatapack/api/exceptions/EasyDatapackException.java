package com.github.imdabigboss.easydatapack.api.exceptions;

/**
 * The base exception for all EasyDatapack exceptions.
 */
public class EasyDatapackException extends Exception {
    public EasyDatapackException(String message) {
        super(message);
    }

    public EasyDatapackException(String message, Throwable cause) {
        super(message, cause);
    }
}
