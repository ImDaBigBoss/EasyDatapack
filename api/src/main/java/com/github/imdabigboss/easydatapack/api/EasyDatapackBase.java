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
 * This is the main class of EasyDatapack. Use {@link EasyDatapackAPI} if you want to interact with EasyDatapack.
 */
public interface EasyDatapackBase {
    void registerCustomAdder(@NonNull Consumer<CustomAdder> customAdder);

    @NonNull YmlConfig getAPIConfig();

    @NonNull GenericBuilder<?> createBuilder(@NonNull Class<? extends GenericBuilder<?>> type, @NonNull Object... args);

    @NonNull RecipeManager getRecipeManager();

    @NonNull BlockManager getBlockManager();

    @NonNull DimensionManager getDimensionManager();

    @NonNull EnchantmentManager getEnchantmentManager();

    @NonNull ItemManager getItemManager();

    @NonNull MapManager getMapManager();

    @NonNull EntityManager getEntityManager();

    @NonNull TexturePackManager getTexturePackManager();
}
