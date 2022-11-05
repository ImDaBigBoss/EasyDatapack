package com.github.imdabigboss.easydatapack.api.exceptions;

/**
 * This exception is thrown when a custom enchantment exception occurs.
 */
public class CustomEnchantmentException extends EasyDatapackException {
    public CustomEnchantmentException(String message) {
        super(message);
    }

    public CustomEnchantmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
