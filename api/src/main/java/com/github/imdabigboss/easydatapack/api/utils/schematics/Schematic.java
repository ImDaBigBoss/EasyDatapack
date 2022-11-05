package com.github.imdabigboss.easydatapack.api.utils.schematics;

import com.github.imdabigboss.easydatapack.api.exceptions.SchematicException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.LimitedRegion;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * This class represents a schematic.
 */
public class Schematic {
    private final BlockData[] blocks;
    private final int width;
    private final int height;
    private final int depth;

    private final int baseWidth;
    private final int baseDepth;
    private final int[] baseOffset;

    public Schematic(@NonNull BlockData[] blocks, int width, int height, int depth, int baseWidth, int baseDepth, int[] baseOffset) {
        this.blocks = blocks;
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.baseWidth = baseWidth;
        this.baseDepth = baseDepth;
        this.baseOffset = baseOffset;
    }

    public @NonNull BlockData[] getBlocks() {
        return blocks;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public int getBaseWidth() {
        return baseWidth;
    }

    public int getBaseDepth() {
        return baseDepth;
    }

    public int[] getBaseOffset() {
        return baseOffset;
    }

    private enum PlacerType {
        LIMITED_REGION,
        LOCATION
    }

    private void placeBlock(Object placer, PlacerType type, int x, int y, int z, BlockData data) throws SchematicException {
        if (type == PlacerType.LIMITED_REGION) {
            LimitedRegion limitedRegion = (LimitedRegion) placer;

            if (limitedRegion.isInRegion(x, y, z)) {
                limitedRegion.setBlockData(x, y, z, data);
            } else {
                throw new SchematicException("Could not place structure block at " + x + " " + y + " " + z + " because it is outside the region");
            }
        } else if (type == PlacerType.LOCATION) {
            Location placeLocation = new Location(((Location) placer).getWorld(), x, y, z);
            placeLocation.getBlock().setBlockData(data);
        } else {
            throw new SchematicException("Unknown placer type " + type.toString());
        }
    }

    private void placeBlock(Object placer, PlacerType type, int x, int y, int z, Material material) throws SchematicException {
        if (type == PlacerType.LIMITED_REGION) {
            LimitedRegion limitedRegion = (LimitedRegion) placer;

            if (limitedRegion.isInRegion(x, y, z)) {
                limitedRegion.setType(x, y, z, material);
            } else {
                throw new SchematicException("Could not place structure block at " + x + " " + y + " " + z + " because it is outside the region");
            }
        } else if (type == PlacerType.LOCATION) {
            Location placeLocation = new Location(((Location) placer).getWorld(), x, y, z);
            placeLocation.getBlock().setType(material);
        } else {
            throw new SchematicException("Unknown placer type: " + type);
        }
    }

    private void pasteSchematic(Object placer, PlacerType type, int placeX, int placeY, int placeZ) throws SchematicException {
        int offsetX = placeX - this.baseOffset[0];
        int offsetY = placeY - this.baseOffset[1];
        int offsetZ = placeZ - this.baseOffset[2];

        int index = 0;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                for (int z = 0; z < this.depth; z++) {
                    BlockData data = this.blocks[index];
                    if (data.getMaterial() != Material.AIR) {
                        if (data.getMaterial() == Material.LIGHT) {
                            this.placeBlock(placer, type, offsetX + x, offsetY + y, offsetZ + z, Material.AIR);
                        } else {
                            this.placeBlock(placer, type, offsetX + x, offsetY + y, offsetZ + z, data);
                        }
                    }
                    index++;
                }
            }
        }
    }

    /**
     * Pastes the schematic at the specified location.
     * @param location the location to paste the schematic at
     */
    public void pasteSchematic(@NonNull Location location) throws SchematicException {
        this.pasteSchematic(location, PlacerType.LOCATION, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Pastes the schematic at the specified location.
     * @param limitedRegion the limited region to paste the schematic at
     */
    public void pasteSchematic(@NonNull LimitedRegion limitedRegion, int placeX, int placeY, int placeZ) throws SchematicException {
        this.pasteSchematic(limitedRegion, PlacerType.LIMITED_REGION, placeX, placeY, placeZ);
    }
}
