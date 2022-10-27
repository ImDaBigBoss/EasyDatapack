package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AmoUtil {
    public static boolean usePlayerAmo(Player player, Integer customModelData) {
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

    public static boolean usePlayerAmo(Player player, Material material) {
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
