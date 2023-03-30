package com.github.imdabigboss.easydatapack.api.utils;

/**
 * This interface represents a generic builder.
 * @param <T> the type of the object to build
 */
public interface GenericBuilder<T> {
    /**
     * Builds the object.
     * @return the built object
     */
    T build();
}
