package com.github.imdabigboss.easydatapack.api.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * This interface represents a generic builder.
 * @param <T> the type of the object to build
 */
public interface GenericBuilder<T> {
    /**
     * Builds the object.
     * @return the built object
     */
    @NonNull T build();
}
