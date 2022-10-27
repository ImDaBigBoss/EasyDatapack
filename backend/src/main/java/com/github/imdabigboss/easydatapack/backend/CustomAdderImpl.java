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

public class CustomAdderImpl extends CustomAdder {
    private final BlockManagerImpl blockManager;
    private final DimensionManagerImpl dimensionManager;
    private final EnchantmentManagerImpl enchantmentManager;
    private final ItemManagerImpl itemManager;
    private final RecipeManagerImpl recipeManager;

    public CustomAdderImpl(EasyDatapack datapack) {
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
    }

    @Override
    public void register(Recipe recipe) throws CustomRecipeException {
        this.recipeManager.registerCustomRecipe(recipe);
    }
}
