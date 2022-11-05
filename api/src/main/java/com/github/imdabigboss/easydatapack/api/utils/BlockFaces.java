package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.block.BlockFace;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An enum of block faces.
 */
public enum BlockFaces {
    NORTH(BlockFace.NORTH, BlockFace.SOUTH, true),
    NORTH_EAST(BlockFace.NORTH_EAST, BlockFace.SOUTH_WEST, false),
    EAST(BlockFace.EAST, BlockFace.WEST, true),
    SOUTH_EAST(BlockFace.SOUTH_EAST, BlockFace.NORTH_WEST, false),
    SOUTH(BlockFace.SOUTH, BlockFace.NORTH, true),
    SOUTH_WEST(BlockFace.SOUTH_WEST, BlockFace.NORTH_EAST, false),
    WEST(BlockFace.WEST, BlockFace.EAST, true),
    NORTH_WEST(BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, false);

    private final BlockFace face;
    private final BlockFace opposite;
    private final boolean simple;

    BlockFaces(@NonNull BlockFace face, @NonNull BlockFace opposite, boolean simple) {
        this.face = face;
        this.opposite = opposite;
        this.simple = simple;
    }

    /**
     * Gets the block face.
     * @return the block face
     */
    public @NonNull BlockFace getFace() {
        return face;
    }

    /**
     * Gets the opposite block face.
     * @return the opposite block face
     */
    public @NonNull BlockFace getOpposite() {
        return opposite;
    }


    /**
     * Checks if the block face is simple, if it isn't a corner.
     * @return true if the block face is simple, false otherwise
     */
    public boolean isSimple() {
        return simple;
    }
}
