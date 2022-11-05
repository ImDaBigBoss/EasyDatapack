package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Some utilities for items.
 */
public class ItemsUtil {
    /**
     * Checks if an item is a tool
     * @param item the item to check
     * @return true if the item is a tool, false otherwise
     */
    public static boolean isItemATool(@NonNull ItemStack item) {
        String name = item.getType().toString();
        return name.endsWith("_SHOVEL") || name.endsWith("_PICKAXE") || name.endsWith("_AXE");
    }

    /**
     * Checks if an item is a sword
     * @param item the item to check
     * @return true if the item is a sword, false otherwise
     */
    public static boolean isItemASword(@NonNull ItemStack item) {
        String name = item.getType().toString();
        return name.endsWith("_SWORD");
    }

    /**
     * Checks if an item is a piece of armor
     * @param item the item to check
     * @return true if the item is a piece of armor, false otherwise
     */
    public static boolean isItemArmor(@NonNull ItemStack item) {
        String name = item.getType().toString();
        return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
    }
}
