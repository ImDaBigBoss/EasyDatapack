package com.github.imdabigboss.easydatapack.backend.utils;

import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class YmlConfigImpl implements YmlConfig {
    private final Plugin plugin;

    public YmlConfigImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NonNull FileConfiguration getConfig() {
        return this.plugin.getConfig();
    }

    @Override
    public void saveConfig() {
        this.plugin.saveConfig();
    }

    @Override
    public void reloadConfig() {
        this.plugin.reloadConfig();
    }
}
