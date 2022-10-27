package com.github.imdabigboss.easydatapack.api.blocks;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    private CustomBlock(String name, String namespaceKey, int customModelData, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, Parent parent, Material dropMaterial, int dropAmount, int dropExperience) {
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

    public String getName() {
        return this.name;
    }

    public String getNamespaceKey() {
        return this.namespaceKey;
    }

    public int getCustomModelData() {
        return this.customModelData;
    }

    public boolean isUp() {
        return this.up;
    }

    public boolean isDown() {
        return this.down;
    }

    public boolean isNorth() {
        return this.north;
    }

    public boolean isSouth() {
        return this.south;
    }

    public boolean isEast() {
        return this.east;
    }

    public boolean isWest() {
        return this.west;
    }

    public int getDropExperience() {
        return this.dropExperience;
    }

    public ItemStack createBlockItem() {
        ItemStack item = new ItemStack(Material.CLOCK, 1);

        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(this.customModelData);
        meta.displayName(Component.text(ChatColor.RESET.toString() + ChatColor.WHITE + this.name));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack createDrop() {
        if (this.dropMaterial == null) {
            ItemStack item = this.createBlockItem();
            item.setAmount(this.dropAmount);
            return item;
        } else {
            return new ItemStack(this.dropMaterial, this.dropAmount);
        }
    }

    public Material getMaterial() {
        return switch (this.parent) {
            case MUSHROOM_STEM -> Material.MUSHROOM_STEM;
            case BROWN_MUSHROOM -> Material.BROWN_MUSHROOM;
            case RED_MUSHROOM -> Material.RED_MUSHROOM;
        };
    }

    public enum Parent {
        MUSHROOM_STEM,
        BROWN_MUSHROOM,
        RED_MUSHROOM
    }

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

        public Builder(String name, String namespaceKey, int customModelData, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, Parent parent) {
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

        public Builder dropMaterial(Material dropMaterial) {
            this.dropMaterial = dropMaterial;
            return this;
        }

        public Builder dropAmount(int dropAmount) {
            this.dropAmount = dropAmount;
            return this;
        }

        public Builder dropExperience(int dropExperience) {
            this.dropExperience = dropExperience;
            return this;
        }

        public CustomBlock build() {
            return new CustomBlock(this.name, this.namespaceKey, this.customModelData, this.up, this.down, this.north, this.east, this.south, this.west, this.parent, this.dropMaterial, this.dropAmount, this.dropExperience);
        }
    }
}
