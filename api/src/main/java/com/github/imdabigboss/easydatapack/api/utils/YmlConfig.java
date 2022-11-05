package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * This class represents a YML configuration file.
 */
public interface YmlConfig {
    /**
     * Gets the {@link FileConfiguration} of this configuration file.
     * @return The {@link FileConfiguration} of this configuration file.
     */
    @NonNull FileConfiguration getConfig();

    /**
     * Saves the configuration file.
     */
    void saveConfig();

    /**
     * Reloads the configuration file.
     */
    void reloadConfig();
}
