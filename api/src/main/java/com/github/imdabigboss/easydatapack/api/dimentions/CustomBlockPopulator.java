package com.github.imdabigboss.easydatapack.api.dimentions;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CustomBlockPopulator extends BlockPopulator {
    private final CustomChunkPopulator[] chunkPopulators;

    public CustomBlockPopulator(CustomChunkPopulator[] chunkPopulators) {
        this.chunkPopulators = chunkPopulators;
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
        for (CustomChunkPopulator chunkPopulator : chunkPopulators) {
            chunkPopulator.populate(worldInfo, random, chunkX, chunkZ, limitedRegion);
        }
    }
}
