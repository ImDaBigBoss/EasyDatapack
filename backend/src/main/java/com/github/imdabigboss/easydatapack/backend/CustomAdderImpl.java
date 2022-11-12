package com.github.imdabigboss.easydatapack.backend;

import com.github.imdabigboss.easydatapack.api.CustomAdder;
import com.github.imdabigboss.easydatapack.api.blocks.CustomBlock;
import com.github.imdabigboss.easydatapack.api.dimentions.CustomDimension;
import com.github.imdabigboss.easydatapack.api.enchantments.CustomEnchantment;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomDimensionException;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomEnchantmentException;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomRecipeException;
import com.github.imdabigboss.easydatapack.api.exceptions.EasyDatapackException;
import com.github.imdabigboss.easydatapack.api.items.CustomItem;
import com.github.imdabigboss.easydatapack.backend.managers.*;
import org.bukkit.inventory.Recipe;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent;
import org.geysermc.geyser.api.item.custom.CustomItemData;
import org.geysermc.geyser.api.item.custom.CustomItemOptions;

public class CustomAdderImpl extends CustomAdder {
    private final Object event;

    private final BlockManagerImpl blockManager;
    private final DimensionManagerImpl dimensionManager;
    private final EnchantmentManagerImpl enchantmentManager;
    private final ItemManagerImpl itemManager;
    private final RecipeManagerImpl recipeManager;

    public CustomAdderImpl(EasyDatapack datapack, Object event) {
        this.event = event;

        this.blockManager = datapack.getBlockManager();
        this.dimensionManager = datapack.getDimensionManager();
        this.enchantmentManager = datapack.getEnchantmentManager();
        this.itemManager = datapack.getItemManager();
        this.recipeManager = datapack.getRecipeManager();
    }

    @Override
    public void register(CustomBlock block) {
        this.blockManager.registerCustomBlock(block);
    }

    @Override
    public void register(CustomDimension dimension) throws CustomDimensionException {
        this.dimensionManager.registerCustomDimension(dimension);
    }

    @Override
    public void register(CustomEnchantment enchantment) throws CustomEnchantmentException {
        this.enchantmentManager.registerCustomEnchantment(enchantment);
    }

    @Override
    public void register(CustomItem item) throws EasyDatapackException {
        this.itemManager.registerCustomItem(item);

        if (this.event != null) {
            CustomItemOptions options = CustomItemOptions.builder()
                    .customModelData(item.getCustomModelData())
                    .build();

            CustomItemData data = CustomItemData.builder()
                    .name(item.getNamespaceKey())
                    .customItemOptions(options)
                    .displayName(item.getName())
                    .build();

            ((GeyserDefineCustomItemsEvent) this.event).register(item.getBaseMaterial().getKey().toString(), data);
        }
    }

    @Override
    public void register(Recipe recipe) throws CustomRecipeException {
        this.recipeManager.registerCustomRecipe(recipe);
    }
}
