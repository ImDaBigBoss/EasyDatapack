package com.github.imdabigboss.easydatapack.api.managers;

import com.github.imdabigboss.easydatapack.api.entities.CustomEntity;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * This class is used to manage custom blocks.
 */
public interface EntityManager {
    /**
     * Gets a list of all registered custom entities.
     * @return a list of all registered custom entities
     */
    @NonNull List<CustomEntity> getCustomEntities();

    /**
     * Spawns a custom entity at the specified location.
     * @param entity the entity to spawn
     * @param location the location to spawn the entity at
     */
    void spawnCustomEntity(@NonNull CustomEntity entity, Location location);
}
