package com.github.imdabigboss.easydatapack.backend.registrar;

import com.github.imdabigboss.easydatapack.api.CustomAdder;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomRecipeException;
import com.github.imdabigboss.easydatapack.backend.CustomAdderImpl;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GenericRegistrar {
    private List<Consumer<CustomAdder>> customAdders = new ArrayList<>();
    private final EasyDatapack easyDatapack;

    public GenericRegistrar(EasyDatapack easyDatapack) {
        this.easyDatapack = easyDatapack;
    }

    public void onEnable() {
        this.onEnableInt();
    }

    public abstract void onEnableInt();

    public void onDisable() {
        try {
            this.easyDatapack.getRecipeManager().unregisterAllRecipes();
        } catch (CustomRecipeException e) {
            e.printStackTrace();
        }

        this.easyDatapack.getEnchantmentManager().unregisterEnchantments();

        this.onDisableInt();
    }

    public abstract void onDisableInt();

    public void registerCustomAdder(@NonNull Consumer<CustomAdder> customAdder) {
        if (this.customAdders == null) {
            throw new RuntimeException("Custom adders can no longer be registered, the plugin has already registered all the custom components");
        }

        this.customAdders.add(customAdder);
    }

    protected void registerCustomComponents(Object event) {
        this.easyDatapack.getLogger().info("Registering custom components.");

        CustomAdderImpl customAdder = new CustomAdderImpl(this.easyDatapack, event);
        for (Consumer<CustomAdder> customAdderConsumer : this.customAdders) {
            customAdderConsumer.accept(customAdder);
        }

        this.customAdders = null;
    }

    protected void finalizeRegistration() {
        this.easyDatapack.getEnchantmentManager().registerEventListeners();
        this.easyDatapack.getItemManager().registerEventListeners();

        this.easyDatapack.getLogger().info("Creating custom dimension worlds.");
        this.easyDatapack.getDimensionManager().createWorlds();

        this.easyDatapack.getLogger().info("Custom component registration complete!");
    }
}
