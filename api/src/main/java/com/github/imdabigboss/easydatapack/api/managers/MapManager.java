package com.github.imdabigboss.easydatapack.api.managers;

import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;

public interface MapManager {
    default ItemStack createMap(String url) {
        try {
            return this.createMap(new URL(url));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    ItemStack createMap(URL url);
}
