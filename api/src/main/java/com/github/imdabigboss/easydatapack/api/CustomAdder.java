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

import java.util.function.Consumer;

/**
 * This class is used to register custom components. It is to be set as an argument in a function that is passed to
 * {@link EasyDatapackAPI#registerCustomAdder(Consumer)}.
 */
public abstract class CustomAdder {
    /**
     * Registers a custom block.
     * @param block the block to register
     */
    public abstract void register(CustomBlock block);

    /**
     * Registers a custom dimension.
     * @param dimension the dimension to register
     * @throws CustomDimensionException if something goes wrong while registering the dimension
     */
    public abstract void register(CustomDimension dimension) throws CustomDimensionException;

    /**
     * Registers a custom enchantment.
     * @param enchantment the enchantment to register
     * @throws CustomEnchantmentException if something goes wrong while registering the enchantment
     */
    public abstract void register(CustomEnchantment enchantment) throws CustomEnchantmentException;

    /**
     * Registers a custom item.
     * @param item the item to register
     * @throws EasyDatapackException if something goes wrong while registering the item
     */
    public abstract void register(CustomItem item) throws EasyDatapackException;

    /**
     * Registers a custom recipe.
     * @param recipe the recipe to register
     * @throws CustomRecipeException if something goes wrong while registering the recipe
     */
    public abstract void register(Recipe recipe) throws CustomRecipeException;
}
