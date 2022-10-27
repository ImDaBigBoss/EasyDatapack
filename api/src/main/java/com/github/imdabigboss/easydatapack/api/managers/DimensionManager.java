package com.github.imdabigboss.easydatapack.api.managers;

import com.github.imdabigboss.easydatapack.api.dimentions.CustomDimension;

import java.util.List;

public interface DimensionManager {
    List<CustomDimension> getCustomDimensions();

    CustomDimension getDimension(String name);
}
