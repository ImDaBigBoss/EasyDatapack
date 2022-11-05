package com.github.imdabigboss.easydatapack.api.managers;

import com.github.imdabigboss.easydatapack.api.items.CustomItem;
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
     * Gets a custom item from its custom model data.
     * @param customModelData the item's custom model data
     * @return the custom item with the given custom model data. Will be null if there is nothing associated with the
     * custom model data.
     */
    @Nullable CustomItem getCustomItem(int customModelData);

    /**
     * Gets an item stack from a custom item's namespace key.
     * @param namespaceKey the namespace key of the custom item
     * @return the custom item's item stack with the given namespace key. Will be null if there is nothing associated
     * with the namespace key.
     */
    @Nullable ItemStack getItemStack(@NonNull String namespaceKey);

    /**
     * Gets an item stack from a custom item's custom model data.
     * @param customModelData the item's custom model data
     * @return the custom item's item stack with the given custom model data. Will be null if there is nothing associated
     * with the custom model data.
     */
    @Nullable ItemStack getItemStack(int customModelData);

    /**
     * Gets if an item stack is a custom hat.
     * @param item the item stack to check
     * @return true if the item stack is a custom hat, false otherwise
     */
    boolean isCustomHat(@NonNull ItemStack item);
}
