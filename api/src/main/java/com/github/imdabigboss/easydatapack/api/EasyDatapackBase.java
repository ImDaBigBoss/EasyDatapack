package com.github.imdabigboss.easydatapack.api;

import com.github.imdabigboss.easydatapack.api.managers.*;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;

import java.util.function.Consumer;

public interface EasyDatapackBase {
    void registerCustomAdder(Consumer<CustomAdder> customAdder);

    YmlConfig getAPIConfig();

    RecipeManager getRecipeManager();

    BlockManager getBlockManager();

    DimensionManager getDimensionManager();

    EnchantmentManager getEnchantmentManager();

    ItemManager getItemManager();

    MapManager getMapManager();
}
