package com.github.imdabigboss.easydatapack.api.blocks;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This class represents a custom block.
 */
public class CustomBlock {
    private final String name;
    private final String namespaceKey;
    private final int customModelData;
    private final boolean up;
    private final boolean down;
    private final boolean north;
    private final boolean south;
    private final boolean east;
    private final boolean west;
    private final Parent parent;
    private final Material dropMaterial;
    private final int dropAmount;
    private final int dropExperience;

    private CustomBlock(@NonNull String name, @NonNull String namespaceKey, int customModelData, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, @NonNull Parent parent, @Nullable Material dropMaterial, int dropAmount, int dropExperience) {
        this.name = name;
        this.namespaceKey = namespaceKey;
        this.customModelData = customModelData;
        this.up = up;
        this.down = down;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.parent = parent;
        this.dropMaterial = dropMaterial;
        this.dropAmount = dropAmount;
        this.dropExperience = dropExperience;
    }

    /**
     * Gets the name of the block.
     * @return the name of the block
     */
    public @NonNull String getName() {
        return this.name;
    }

    /**
     * Gets the namespace key of the block.
     * @return the namespace key of the block
     */
    public @NonNull String getNamespaceKey() {
        return this.namespaceKey;
    }

    /**
     * Gets the custom model data of the block's item.
     * @return the custom model data of the block's item
     */
    public int getCustomModelData() {
        return this.customModelData;
    }

    /**
     * Gets the up state of the block.
     * @return the up state of the block
     */
    public boolean isUp() {
        return this.up;
    }

    /**
     * Gets the down state of the block.
     * @return the down state of the block
     */
    public boolean isDown() {
        return this.down;
    }

    /**
     * Gets the north state of the block.
     * @return the north state of the block
     */
    public boolean isNorth() {
        return this.north;
    }

    /**
     * Gets the east state of the block.
     * @return the east state of the block
     */
    public boolean isSouth() {
        return this.south;
    }

    /**
     * Gets the east state of the block.
     * @return the east state of the block
     */
    public boolean isEast() {
        return this.east;
    }

    /**
     * Gets the west state of the block.
     * @return the west state of the block
     */
    public boolean isWest() {
        return this.west;
    }

    /**
     * Gets the parent of the block.
     * @return the parent of the block
     */
    public @NonNull Parent getParent() {
        return this.parent;
    }

    /**
     * Gets the dropped experience of the block.
     * @return the dropped experience of the block
     */
    public int getDropExperience() {
        return this.dropExperience;
    }

    /**
     * Gets the dropped material of the block.
     * @return the dropped material of the block
     */
    public @Nullable Material getDropMaterial() {
        return this.dropMaterial;
    }

    /**
     * Gets the dropped amount of the block.
     * @return the dropped amount of the block
     */
    public int getDropAmount() {
        return this.dropAmount;
    }

    /**
     * Creates the item used to place the block.
     * @return the item used to place the block
     */
    public @NonNull ItemStack createBlockItem() {
        ItemStack item = new ItemStack(Material.CLOCK, 1);

        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(this.customModelData);
        meta.displayName(Component.text(ChatColor.RESET.toString() + ChatColor.WHITE + this.name));
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Creates a drop for the block.
     * @return the drop for the block
     */
    public @NonNull ItemStack createDrop() {
        if (this.dropMaterial == null) {
            ItemStack item = this.createBlockItem();
            item.setAmount(this.dropAmount);
            return item;
        } else {
            return new ItemStack(this.dropMaterial, this.dropAmount);
        }
    }

    /**
     * Gets the base block material.
     * @return the base block material
     */
    public @NonNull Material getMaterial() {
        return switch (this.parent) {
            case MUSHROOM_STEM -> Material.MUSHROOM_STEM;
            case BROWN_MUSHROOM -> Material.BROWN_MUSHROOM;
            case RED_MUSHROOM -> Material.RED_MUSHROOM;
        };
    }

    /**
     * This enum represents the parent of a custom block, the block type used to display our custom block.
     */
    public enum Parent {
        MUSHROOM_STEM,
        BROWN_MUSHROOM,
        RED_MUSHROOM
    }

    /**
     * This class represents a custom block builder.
     */
    public static class Builder {
        private final String name;
        private final String namespaceKey;
        private final int customModelData;
        private final boolean up;
        private final boolean down;
        private final boolean north;
        private final boolean south;
        private final boolean east;
        private final boolean west;
        private final Parent parent;

        private Material dropMaterial = null;
        private int dropAmount = 1;
        private int dropExperience = 0;

        /**
         * Creates a new custom block builder.
         * @param name the name of the block
         * @param namespaceKey the namespace key of the block
         * @param customModelData the custom model data of the block (used for the item texture in the inventory)
         * @param up up state
         * @param down down state
         * @param north north state
         * @param east east state
         * @param south south state
         * @param west west state
         * @param parent the parent mushroom stem
         */
        public Builder(@NonNull String name, @NonNull String namespaceKey, int customModelData, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, @NonNull Parent parent) {
            this.name = name;
            this.namespaceKey = namespaceKey;
            this.customModelData = customModelData;
            this.up = up;
            this.down = down;
            this.north = north;
            this.south = south;
            this.east = east;
            this.west = west;
            this.parent = parent;
        }

        /**
         * Sets the drop material of the block.
         * @param dropMaterial the drop material of the block, set to null to drop the block itself
         * @return the builder
         */
        public @NonNull Builder dropMaterial(@Nullable Material dropMaterial) {
            this.dropMaterial = dropMaterial;
            return this;
        }

        /**
         * Sets the drop amount of the block.
         * @param dropAmount the drop amount of the block
         * @return the builder
         */
        public @NonNull Builder dropAmount(int dropAmount) {
            this.dropAmount = dropAmount;
            return this;
        }

        /**
         * Sets the drop experience of the block.
         * @param dropExperience the drop experience of the block
         * @return the builder
         */
        public @NonNull Builder dropExperience(int dropExperience) {
            this.dropExperience = dropExperience;
            return this;
        }

        /**
         * Builds the custom block.
         * @return the custom block
         */
        public @NonNull CustomBlock build() {
            return new CustomBlock(this.name, this.namespaceKey, this.customModelData, this.up, this.down, this.north, this.east, this.south, this.west, this.parent, this.dropMaterial, this.dropAmount, this.dropExperience);
        }
    }
}
