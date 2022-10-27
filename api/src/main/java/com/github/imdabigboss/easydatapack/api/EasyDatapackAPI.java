package com.github.imdabigboss.easydatapack.api;

import com.github.imdabigboss.easydatapack.api.managers.*;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;

import java.util.function.Consumer;

public class EasyDatapackAPI {
    private static EasyDatapackBase api = null;

    public static final String NAMESPACE_KEY = "easydatapack";

    public static void set(EasyDatapackBase api) {
        if (EasyDatapackAPI.api != null) {
            throw new RuntimeException("EasyDatapackAPI is already set!");
        }

        EasyDatapackAPI.api = api;
    }

    public static void registerCustomAdder(Consumer<CustomAdder> customAdder) {
        api.registerCustomAdder(customAdder);
    }

    public static YmlConfig getAPIConfig() {
        return api.getAPIConfig();
    }

    public static RecipeManager getRecipeManager() {
        return api.getRecipeManager();
    }

    public static BlockManager getBlockManager() {
        return api.getBlockManager();
    }

    public static DimensionManager getDimensionManager() {
        return api.getDimensionManager();
    }

    public static EnchantmentManager getEnchantmentManager() {
        return api.getEnchantmentManager();
    }

    public static ItemManager getItemManager() {
        return api.getItemManager();
    }

    public static MapManager getMapManager() {
        return api.getMapManager();
    }
}
