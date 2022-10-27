package com.github.imdabigboss.easydatapack.api.managers;

import com.github.imdabigboss.easydatapack.api.enchantments.CustomEnchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface EnchantmentManager {
    List<CustomEnchantment> getEnchantments();

    void updateItemLoreEnchants(ItemStack item);

    void reformatItemNameColours(ItemStack original, ItemStack result);
}
