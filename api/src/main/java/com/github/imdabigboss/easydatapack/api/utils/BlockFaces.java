package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.block.BlockFace;

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

    BlockFaces(BlockFace face, BlockFace opposite, boolean simple) {
        this.face = face;
        this.opposite = opposite;
        this.simple = simple;
    }

    public BlockFace getFace() {
        return face;
    }

    public BlockFace getOpposite() {
        return opposite;
    }

    public boolean isSimple() {
        return simple;
    }
}
