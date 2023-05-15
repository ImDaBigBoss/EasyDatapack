package com.github.imdabigboss.easydatapack.api.types.dimentions;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

/**
 * A custom block populator.
 */
public class CustomBlockPopulator extends BlockPopulator {
    private final CustomChunkPopulator[] chunkPopulators;

    /**
     * Creates a new custom block populator.
     * @param chunkPopulators the chunk populators used to generate the world
     */
    public CustomBlockPopulator(CustomChunkPopulator[] chunkPopulators) {
        this.chunkPopulators = chunkPopulators;
    }

    @Override
    public void populate(@NonNull WorldInfo worldInfo, @NonNull Random random, int chunkX, int chunkZ, @NonNull LimitedRegion limitedRegion) {
        for (CustomChunkPopulator chunkPopulator : chunkPopulators) {
            chunkPopulator.populate(worldInfo, random, chunkX, chunkZ, limitedRegion);
        }
    }
}
