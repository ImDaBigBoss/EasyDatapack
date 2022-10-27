package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.inventory.ItemStack;

public class ItemsUtil {
    public static boolean isItemATool(ItemStack item) {
        if (item == null) {
            return false;
        }
        String name = item.getType().toString();

        return name.endsWith("_SHOVEL") || name.endsWith("_PICKAXE") || name.endsWith("_AXE");
    }

    public static boolean isItemASword(ItemStack item) {
        if (item == null) {
            return false;
        }
        String name = item.getType().toString();

        return name.endsWith("_SWORD");
    }

    public static boolean isItemArmor(ItemStack item) {
        if (item == null) {
            return false;
        }
        String name = item.getType().toString();

        return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
    }
}
