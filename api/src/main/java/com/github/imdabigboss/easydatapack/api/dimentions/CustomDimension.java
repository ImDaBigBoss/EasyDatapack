package com.github.imdabigboss.easydatapack.api.dimentions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BiomeProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This class represents a custom dimension.
 */
public abstract class CustomDimension {
    /**
     * Gets the material used as a portal frame block.
     * @return the material used as a portal frame block
     */
    public abstract @NonNull Material getPortalFrameMaterial();

    /**
     * Gets the name of the dimension.
     * @return the name of the dimension
     */
    public abstract @NonNull String getName();

    /**
     * Gets the world environment of the dimension: NETHER, THE_END, NORMAL, or CUSTOM.
     * @return the world environment of the dimension
     */
    public abstract  World.@NonNull Environment getEnvironment();

    /**
     * Converts a location from the custom dimension to the overworld.
     * @param location the location in the custom dimension
     * @return the location in the overworld
     */
    public abstract @NonNull Location dimensionToNormal(Location location);

    /**
     * Converts a location from the overworld to the custom dimension.
     * @param location the location in the overworld
     * @return the location in the custom dimension
     */
    public abstract @NonNull Location normalToDimension(Location location);

    /**
     * Gets the biome provider of the dimension.
     * @return the biome provider of the dimension
     */
    protected abstract @NonNull BiomeProvider getBiomeProvider();

    /**
     * Gets the chunk generator of the dimension.
     * @return the chunk generator of the dimension
     */
    protected abstract @NonNull CustomChunkGenerator getChunkGenerator();

    /**
     * Gets the name of the given biome in this custom dimension.
     * @param biome the biome
     * @return the name of the biome
     */
    public abstract @NonNull String getBiomeName(Biome biome);

    /**
     * This function is called by the spawn entity event, when an entity is spawned naturally in the dimension.
     * @param location the location where the entity is spawned
     * @param original the original entity type
     * @param spawned the entity type that will be spawned
     * @return if the entity should be allowed to spawn
     */
    public abstract boolean spawnEntity(@NonNull Location location, @NonNull EntityType original, @NonNull Entity spawned);

    /**
     * Creates the world for this dimension.
     * @param seed the seed of the world
     * @return the world
     */
    public @Nullable World createWorld(long seed) {
        WorldCreator worldCreator = new WorldCreator(this.getName());
        worldCreator.biomeProvider(this.getBiomeProvider());
        worldCreator.generator(this.getChunkGenerator());
        worldCreator.environment(this.getEnvironment());
        worldCreator.generateStructures(false);
        worldCreator.hardcore(false);
        worldCreator.seed(seed);

        return worldCreator.createWorld();
    }
}
