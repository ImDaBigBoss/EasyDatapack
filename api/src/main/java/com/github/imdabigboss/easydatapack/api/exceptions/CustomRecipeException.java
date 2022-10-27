package com.github.imdabigboss.easydatapack.api.exceptions;

public class CustomRecipeException extends EasyDatapackException {
    public CustomRecipeException(String message) {
        super(message);
    }

    public CustomRecipeException(String message, Throwable cause) {
        super(message, cause);
    }
}
