package com.github.imdabigboss.easydatapack.backend.types.blocks;

import com.github.imdabigboss.easydatapack.api.types.blocks.CustomBlock;
import com.github.imdabigboss.easydatapack.api.types.items.CustomBlockPlacerItem;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CustomBlockImpl implements CustomBlock {
    private final String name;
    private final String namespaceKey;
    private final CustomBlockPlacerItem placerItem;
    private final boolean up;
    private final boolean down;
    private final boolean north;
    private final boolean south;
    private final boolean east;
    private final boolean west;
    private final CustomBlock.Parent parent;
    private final Material dropMaterial;
    private final int dropAmount;
    private final int dropExperience;

    private CustomBlockImpl(@NonNull String name, @NonNull String namespaceKey, @NonNull CustomBlockPlacerItem placerItem, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, CustomBlock.@NonNull Parent parent, @Nullable Material dropMaterial, int dropAmount, int dropExperience) {
        this.name = name;
        this.namespaceKey = namespaceKey;
        this.placerItem = placerItem;
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

    @Override
    public @NonNull String getName() {
        return this.name;
    }

    @Override
    public @NonNull String getNamespaceKey() {
        return this.namespaceKey;
    }

    @Override
    public @NonNull CustomBlockPlacerItem getPlacerItem() {
        return this.placerItem;
    }

    @Override
    public boolean isUp() {
        return this.up;
    }

    @Override
    public boolean isDown() {
        return this.down;
    }

    @Override
    public boolean isNorth() {
        return this.north;
    }

    @Override
    public boolean isSouth() {
        return this.south;
    }

    @Override
    public boolean isEast() {
        return this.east;
    }

    @Override
    public boolean isWest() {
        return this.west;
    }

    @Override
    public CustomBlock.@NonNull Parent getParent() {
        return this.parent;
    }

    @Override
    public int getDropExperience() {
        return this.dropExperience;
    }

    @Override
    public @Nullable Material getDropMaterial() {
        return this.dropMaterial;
    }

    @Override
    public int getDropAmount() {
        return this.dropAmount;
    }

    @Override
    public @NonNull ItemStack createDrop() {
        if (this.dropMaterial == null) {
            ItemStack item = this.placerItem.createItemStack();
            item.setAmount(this.dropAmount);
            return item;
        } else {
            return new ItemStack(this.dropMaterial, this.dropAmount);
        }
    }

    @Override
    public @NonNull Material getMaterial() {
        return switch (this.parent) {
            case MUSHROOM_STEM -> Material.MUSHROOM_STEM;
            case BROWN_MUSHROOM -> Material.BROWN_MUSHROOM;
            case RED_MUSHROOM -> Material.RED_MUSHROOM;
        };
    }

    public static class BuilderImpl implements Builder, GenericBuilderImpl {
        private final String name;
        private final String namespaceKey;
        private final CustomBlockPlacerItem placerItem;
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

        public BuilderImpl(@NonNull String name, @NonNull String namespaceKey, @NonNull CustomBlockPlacerItem placerItem, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, @NonNull Parent parent) {
            this.name = name;
            this.namespaceKey = namespaceKey;
            this.placerItem = placerItem;
            this.up = up;
            this.down = down;
            this.north = north;
            this.south = south;
            this.east = east;
            this.west = west;
            this.parent = parent;
        }

        @Override
        public @NonNull Builder dropMaterial(@Nullable Material dropMaterial) {
            this.dropMaterial = dropMaterial;
            return this;
        }

        @Override
        public @NonNull Builder dropAmount(int dropAmount) {
            this.dropAmount = dropAmount;
            return this;
        }

        @Override
        public @NonNull Builder dropExperience(int dropExperience) {
            this.dropExperience = dropExperience;
            return this;
        }

        @Override
        public @NonNull CustomBlock build() {
            return new CustomBlockImpl(this.name, this.namespaceKey, this.placerItem, this.up, this.down, this.north, this.east, this.south, this.west, this.parent, this.dropMaterial, this.dropAmount, this.dropExperience);
        }
    }
}
