package com.github.imdabigboss.easydatapack.api.types.dimentions;

import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A custom chunk generator.
 */
public abstract class CustomChunkGenerator extends ChunkGenerator {
    protected final BiomeProvider biomeProvider;
    private final List<BlockPopulator> blockPopulators;

    /**
     * Creates a new custom chunk generator.
     * @param biomeProvider the biome provider
     * @param chunkPopulators the chunk populators used to generate the world
     */
    public CustomChunkGenerator(@NonNull BiomeProvider biomeProvider, @Nullable CustomChunkPopulator... chunkPopulators) {
        this.biomeProvider = biomeProvider;

        for (CustomChunkPopulator chunkPopulator : chunkPopulators) {
            chunkPopulator.setBiomeProvider(this.biomeProvider);
        }

        if (chunkPopulators == null || chunkPopulators.length == 0) {
            this.blockPopulators = new ArrayList<>();
        } else {
            this.blockPopulators = new ArrayList<>(List.of(new CustomBlockPopulator(chunkPopulators)));
        }
    }

    /**
     * Creates a new custom chunk generator.
     * @param biomeProvider the biome provider
     */
    public CustomChunkGenerator(@NonNull BiomeProvider biomeProvider) {
        this.biomeProvider = biomeProvider;
        this.blockPopulators = new ArrayList<>();
    }

    @Override
    public abstract void generateNoise(@NonNull WorldInfo worldInfo, @NonNull Random random, int chunkX, int chunkZ, ChunkGenerator.@NonNull ChunkData chunkData);

    public @NonNull List<BlockPopulator> getDefaultPopulators(@NonNull World world) {
        return this.blockPopulators;
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NonNull WorldInfo worldInfo) {
        return this.biomeProvider;
    }

    @Override
    public abstract boolean shouldGenerateSurface();

    @Override
    public abstract boolean shouldGenerateBedrock();

    @Override
    public abstract boolean shouldGenerateCaves();

    @Override
    public abstract boolean shouldGenerateDecorations();

    @Override
    public abstract boolean shouldGenerateMobs();

    @Override
    public abstract boolean shouldGenerateStructures();
}
