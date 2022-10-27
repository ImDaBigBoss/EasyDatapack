package com.github.imdabigboss.easydatapack.api.managers;

import com.github.imdabigboss.easydatapack.api.items.CustomItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemManager {
    List<CustomItem> getCustomItems();

    CustomItem getCustomItem(String namespaceKey);

    CustomItem getCustomItem(Integer customModelData);

    ItemStack getItemStack(String namespaceKey);

    ItemStack getItemStack(Integer customModelData);

    boolean isCustomHat(ItemStack item);
}
