package com.github.imdabigboss.easydatapack.backend.utils;

import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.bukkit.event.Listener;

public abstract class GenericManager implements Listener {
    protected final EasyDatapack datapack;

    public GenericManager(EasyDatapack datapack) {
        this.datapack = datapack;
        this.registerBuilders();
    }

    public void registerListener() {
        this.datapack.getServer().getPluginManager().registerEvents(this, this.datapack);
    }

    public abstract void registerBuilders();
}
