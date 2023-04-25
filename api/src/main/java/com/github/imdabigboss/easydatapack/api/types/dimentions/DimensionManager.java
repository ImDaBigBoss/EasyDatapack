package com.github.imdabigboss.easydatapack.api.types.dimentions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * This class is used to manage custom dimensions.
 */
public interface DimensionManager {
    /**
     * Gets a list of all registered custom dimensions.
     * @return a list of all registered custom dimensions
     */
    @NonNull List<CustomDimension> getCustomDimensions();

    /**
     * Gets a custom dimension from a name.
     * @param name the name of the custom dimension
     * @return the custom dimension with the given name. Will be null if the dimension does not exist.
     */
    @Nullable CustomDimension getDimension(@NonNull String name);
}
