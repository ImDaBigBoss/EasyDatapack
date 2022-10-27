package com.github.imdabigboss.easydatapack.api;

import com.github.imdabigboss.easydatapack.api.blocks.CustomBlock;
import com.github.imdabigboss.easydatapack.api.dimentions.CustomDimension;
import com.github.imdabigboss.easydatapack.api.enchantments.CustomEnchantment;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomDimensionException;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomEnchantmentException;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomRecipeException;
import com.github.imdabigboss.easydatapack.api.exceptions.EasyDatapackException;
import com.github.imdabigboss.easydatapack.api.items.CustomItem;
import org.bukkit.inventory.Recipe;

public abstract class CustomAdder {
    public abstract void register(CustomBlock block);

    public abstract void register(CustomDimension dimension) throws CustomDimensionException;

    public abstract void register(CustomEnchantment enchantment) throws CustomEnchantmentException;

    public abstract void register(CustomItem item) throws EasyDatapackException;

    public abstract void register(Recipe recipe) throws CustomRecipeException;
}
