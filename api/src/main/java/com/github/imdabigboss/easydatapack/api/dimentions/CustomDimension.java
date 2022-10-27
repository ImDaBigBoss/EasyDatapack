package com.github.imdabigboss.easydatapack.api.dimentions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BiomeProvider;

public abstract class CustomDimension {
    public abstract Material getPortalFrameMaterial();

    public abstract String getName();

    public abstract World.Environment getEnvironment();

    public abstract Location dimensionToNormal(Location location);

    public abstract Location normalToDimension(Location location);

    protected abstract BiomeProvider getBiomeProvider();

    protected abstract CustomChunkGenerator getChunkGenerator();

    public abstract String getBiomeName(Biome biome);

    public abstract boolean spawnEntity(Location location, EntityType original, Entity spawned);

    public World createWorld(long seed) {
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
