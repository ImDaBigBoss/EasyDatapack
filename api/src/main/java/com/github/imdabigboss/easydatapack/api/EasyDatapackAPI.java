package com.github.imdabigboss.easydatapack.api;

import com.github.imdabigboss.easydatapack.api.managers.*;
import com.github.imdabigboss.easydatapack.api.utils.PacketUtil;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

/**
 * This class is used to get functions from the instance of {@link EasyDatapackBase}.
 */
public class EasyDatapackAPI {
    private static EasyDatapackBase api = null;

    /**
     * The EasyDatapack namespace key.
     */
    public static final String NAMESPACE_KEY = "easydatapack";

    /**
     * Sets the EasyDatapack API instace. This should only be called by EasyDatapack.
     * @param api the EasyDatapack API instance
     */
    public static void set(@NonNull EasyDatapackBase api) {
        if (EasyDatapackAPI.api != null) {
            throw new RuntimeException("EasyDatapackAPI is already set!");
        }

        EasyDatapackAPI.api = api;
    }

    /**
     * Registers function that takes a {@link CustomAdder} as an argument and that will be called when EasyDatapack is
     * enabled to register custom components.
     * @param customAdder the custom adder
     */
    public static void registerCustomAdder(@NonNull Consumer<CustomAdder> customAdder) {
        api.registerCustomAdder(customAdder);
    }

    /**
     * Gets the EasyDatapack config.
     * @return the EasyDatapack config
     */
    public static @NonNull YmlConfig getAPIConfig() {
        return api.getAPIConfig();
    }

    /**
     * Gets the EasyDatapack packet utility.
     * @return the EasyDatapack packet utility
     */
    public static @NonNull PacketUtil getPacketUtil() {
        return api.getPacketUtil();
    }

    /**
     * Gets the EasyDatapack recipe manager.
     * @return the EasyDatapack recipe manager
     */
    public static @NonNull RecipeManager getRecipeManager() {
        return api.getRecipeManager();
    }

    /**
     * Gets the EasyDatapack block manager.
     * @return the EasyDatapack block manager
     */
    public static @NonNull BlockManager getBlockManager() {
        return api.getBlockManager();
    }

    /**
     * Gets the EasyDatapack dimension manager.
     * @return the EasyDatapack dimension manager
     */
    public static @NonNull DimensionManager getDimensionManager() {
        return api.getDimensionManager();
    }

    /**
     * Gets the EasyDatapack enchantment manager.
     * @return the EasyDatapack enchantment manager
     */
    public static @NonNull EnchantmentManager getEnchantmentManager() {
        return api.getEnchantmentManager();
    }

    /**
     * Gets the EasyDatapack item manager.
     * @return the EasyDatapack item manager
     */
    public static @NonNull ItemManager getItemManager() {
        return api.getItemManager();
    }

    /**
     * Gets the EasyDatapack map manager.
     * @return the EasyDatapack map manager
     */
    public static @NonNull MapManager getMapManager() {
        return api.getMapManager();
    }
}
