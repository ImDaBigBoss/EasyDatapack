package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Some utilities for ammunition.
 */
public class AmmunitionUtil {
    /**
     * Checks if a player has an item with the given custom model data and removes one from the player's inventory.
     * @param player the player
     * @param customModelData the custom model data
     * @return true if the player had the item, false otherwise
     */
    public static boolean usePlayerAmo(@NonNull Player player, int customModelData) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return true;
        }

        int slot = -1;
        for (int i = 0; i < 35; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) {
                continue;
            }
            if (item.getItemMeta() == null) {
                continue;
            }
            if (!item.getItemMeta().hasCustomModelData()) {
                continue;
            }

            if (item.getItemMeta().getCustomModelData() == customModelData) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            ItemStack item = player.getInventory().getItemInOffHand();
            if (item.getItemMeta() == null) {
                return false;
            }
            if (!item.getItemMeta().hasCustomModelData()) {
                return false;
            }

            if (item.getItemMeta().getCustomModelData() == customModelData) {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                return true;
            }

            return false;
        } else {
            player.getInventory().getItem(slot).setAmount(player.getInventory().getItem(slot).getAmount() - 1);
            return true;
        }
    }

    /**
     * Checks if a player has an item with the given material and removes one from the player's inventory.
     * @param player the player
     * @param material the material
     * @return true if the player had the item, false otherwise
     */
    public static boolean usePlayerAmo(@NonNull Player player, @NonNull Material material) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return true;
        }

        int slot = -1;
        for (int i = 0; i < 35; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) {
                continue;
            }

            if (item.getType() == material) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            ItemStack item = player.getInventory().getItemInOffHand();

            if (item.getType() == material) {
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                return true;
            }

            return false;
        } else {
            player.getInventory().getItem(slot).setAmount(player.getInventory().getItem(slot).getAmount() - 1);
            return true;
        }
    }
}
