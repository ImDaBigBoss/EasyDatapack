package com.github.imdabigboss.easydatapack.api.enchantments;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * This class is used to manage custom enchantments.
 */
public interface EnchantmentManager {
    /**
     * Gets a list of all registered custom enchantments.
     * @return a list of all registered custom enchantments
     */
    @NonNull List<CustomEnchantment> getEnchantments();

    /**
     * Update the item enchantment list lore. This exists because custom enchantments don't show up by default, so need
     * to be added in manually to the lore.
     * @param item the item to update the lore of
     */
    void updateItemLoreEnchants(@NonNull ItemStack item);

    /**
     * This is used if an item is crafted with, or put into something like an anvil. We are removing the colour codes from
     * the raw name.
     * @param result the result item that needs its name reformatting
     */
    void reformatItemNameColours(@NonNull ItemStack result);
}
