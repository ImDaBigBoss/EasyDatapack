package com.github.imdabigboss.easydatapack.api;

import com.github.imdabigboss.easydatapack.api.textures.TexturePackManager;
import com.github.imdabigboss.easydatapack.api.types.blocks.BlockManager;
import com.github.imdabigboss.easydatapack.api.types.dimentions.DimensionManager;
import com.github.imdabigboss.easydatapack.api.types.enchantments.EnchantmentManager;
import com.github.imdabigboss.easydatapack.api.types.entities.EntityManager;
import com.github.imdabigboss.easydatapack.api.types.items.ItemManager;
import com.github.imdabigboss.easydatapack.api.types.maps.MapManager;
import com.github.imdabigboss.easydatapack.api.types.recipies.RecipeManager;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
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
     * Creates a builder of the specified type.
     * @param type the type of the builder
     * @param args the arguments to pass to the builder
     * @return the builder
     */
    public static @NonNull GenericBuilder<?> createBuilder(@NonNull Class<? extends GenericBuilder<?>> type, @NonNull Object... args) {
        return api.createBuilder(type, args);
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

    /**
     * Gets the EasyDatapack entity manager.
     * @return the EasyDatapack entity manager
     */
    public static @NonNull EntityManager getEntityManager() {
        return api.getEntityManager();
    }

    /**
     * Gets the EasyDatapack texture pack manager.
     * @return the EasyDatapack texture manager
     */
    public static @NonNull TexturePackManager getTexturePackManager() {
        return api.getTexturePackManager();
    }
}
