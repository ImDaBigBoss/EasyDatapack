package com.github.imdabigboss.easydatapack.backend.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PDCUtil {
    public static void setData(ItemStack item, NamespacedKey key, String value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, value);

        item.setItemMeta(meta);
    }

    public static String getData(ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.STRING)) {
            return container.get(key, PersistentDataType.STRING);
        }

        return null;
    }

    public static void removeData(ItemStack item, NamespacedKey key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.remove(key);

        item.setItemMeta(meta);
    }
}
