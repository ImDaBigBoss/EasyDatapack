package com.github.imdabigboss.easydatapack.api.maps;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is used to manage custom maps.
 */
public interface MapManager {
    /**
     * Creates an item stack of a map from a URL.
     * @param url the URL of the map
     * @return the item stack of the map
     */
    default @Nullable ItemStack createMap(@NonNull String url) {
        try {
            return this.createMap(new URL(url));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Creates an item stack of a map from a URL.
     * @param url the URL of the map
     * @return the item stack of the map
     */
    @Nullable ItemStack createMap(@NonNull URL url);
}
