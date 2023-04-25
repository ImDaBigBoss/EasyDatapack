package com.github.imdabigboss.easydatapack.api.types.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * This class is used to manage custom items.
 */
public interface ItemManager {
    /**
     * Gets a list of all registered custom items.
     * @return a list of all registered custom items
     */
    @NonNull List<CustomItem> getCustomItems();

    /**
     * Gets a custom item from its namespace key.
     * @param namespaceKey the namespace key of the custom item
     * @return the custom item with the given namespace key. Will be null if there is nothing associated with the
     * namespace key.
     */
    @Nullable CustomItem getCustomItem(@NonNull String namespaceKey);

    /**
     * Gets a custom item from its custom model data and material.
     * @param customModelData the item's custom model data
     * @return the custom item with the given custom model data. Will be null if there is nothing associated with the
     * custom model data and material.
     */
    @Nullable CustomItem getCustomItem(int customModelData, Material material);

    /**
     * Gets a custom item from its item stack.
     * @param item the item stack to get the custom item from
     * @return the custom item or null if it is not a custom item
     */
    @Nullable CustomItem getCustomItem(@NonNull ItemStack item);

    /**
     * Gets if an item stack is a custom hat.
     * @param item the item stack to check
     * @return true if the item stack is a custom hat, false otherwise
     */
    boolean isCustomHat(@NonNull ItemStack item);
}
