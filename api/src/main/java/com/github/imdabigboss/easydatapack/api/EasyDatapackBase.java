package com.github.imdabigboss.easydatapack.api;

import com.github.imdabigboss.easydatapack.api.blocks.BlockManager;
import com.github.imdabigboss.easydatapack.api.dimentions.DimensionManager;
import com.github.imdabigboss.easydatapack.api.enchantments.EnchantmentManager;
import com.github.imdabigboss.easydatapack.api.entities.EntityManager;
import com.github.imdabigboss.easydatapack.api.items.ItemManager;
import com.github.imdabigboss.easydatapack.api.maps.MapManager;
import com.github.imdabigboss.easydatapack.api.recipies.RecipeManager;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
import com.github.imdabigboss.easydatapack.api.utils.PacketUtil;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

/**
 * This is the main class of EasyDatapack. Use {@link EasyDatapackAPI} if you want to interact with EasyDatapack.
 */
public interface EasyDatapackBase {
    void registerCustomAdder(@NonNull Consumer<CustomAdder> customAdder);

    @NonNull YmlConfig getAPIConfig();

    @NonNull PacketUtil getPacketUtil();

    @NonNull RecipeManager getRecipeManager();

    @NonNull BlockManager getBlockManager();

    @NonNull DimensionManager getDimensionManager();

    @NonNull EnchantmentManager getEnchantmentManager();

    @NonNull ItemManager getItemManager();

    @NonNull MapManager getMapManager();

    @NonNull EntityManager getEntityManager();

    @NonNull GenericBuilder<?> createBuilder(@NonNull Class<? extends GenericBuilder<?>> type, @NonNull Object... args);
}
