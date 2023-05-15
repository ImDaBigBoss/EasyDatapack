package com.github.imdabigboss.easydatapack.api.types.dimentions;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;

/**
 * A custom chunk populator, used to generate the world.
 */
public abstract class CustomChunkPopulator {
    private BiomeProvider biomeProvider;

    protected BiomeProvider getBiomeProvider() {
        return biomeProvider;
    }

    /**
     * Sets the biome provider.
     * @param biomeProvider the biome provider
     */
    public void setBiomeProvider(@NonNull BiomeProvider biomeProvider) {
        this.biomeProvider = biomeProvider;
    }

    /**
     * Generates the world.
     * @param worldInfo the world info
     * @param random the seeded random number generator
     * @param chunkX the chunk x
     * @param chunkZ the chunk z
     * @param limitedRegion the limited region
     */
    public abstract void populate(@NonNull WorldInfo worldInfo, @NonNull Random random, int chunkX, int chunkZ, @NonNull LimitedRegion limitedRegion);

    /**
     * Gets if a material is what can be found at the surface of the world.
     * @param cover the cover material to check, grass for example
     * @param ground the ground material to check, dirt or grass block for example
     * @param biome the biome to check
     * @return if the material is what can be found at the surface of the world
     */
    public abstract boolean isSurface(@NonNull Material cover, @NonNull Material ground, @NonNull Biome biome);

    //TODO: Find a better way to do this. Searching from top to bottom to find the top layer is not very efficient.
    protected @Nullable LocationData getRandomSurfaceXYZ(@NonNull WorldInfo worldInfo, @NonNull Random random, int chunkX, int chunkZ, @NonNull LimitedRegion limitedRegion) {
        int x = random.nextInt(16) + (chunkX * 16);
        int y = worldInfo.getMaxHeight();
        int z = random.nextInt(16) + (chunkZ * 16);

        Biome biome = this.getBiomeProvider().getBiome(worldInfo, x, 0, z);

        while (y > worldInfo.getMinHeight() + 1) {
            y -= 1;
            if (this.isSurface(limitedRegion.getType(x, y, z), limitedRegion.getType(x, y - 1, z), biome)) {
                return new LocationData(x, y, z, biome);
            }
        }

        return null;
    }

    protected record LocationData(int x, int y, int z, @NonNull Biome biome) {
    }
}
