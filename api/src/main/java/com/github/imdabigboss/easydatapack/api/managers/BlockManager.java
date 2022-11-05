package com.github.imdabigboss.easydatapack.api.managers;

import com.github.imdabigboss.easydatapack.api.blocks.CustomBlock;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * This class is used to manage custom blocks.
 */
public interface BlockManager {
    /**
     * Gets a list of all registered custom blocks.
     * @return a list of all registered custom blocks
     */
    @NonNull List<CustomBlock> getCustomBlocks();

    /**
     * Gets a custom from a {@link Block}.
     * @param block the block to get the custom block from
     * @return the custom block with the given name. Will be null if the block is not a custom block.
     */
    @Nullable CustomBlock blockToCustomBlock(@NonNull Block block);
}
