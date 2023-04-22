package com.github.imdabigboss.easydatapack.api;

import com.github.imdabigboss.easydatapack.api.blocks.CustomBlock;
import com.github.imdabigboss.easydatapack.api.dimentions.CustomDimension;
import com.github.imdabigboss.easydatapack.api.enchantments.CustomEnchantment;
import com.github.imdabigboss.easydatapack.api.entities.CustomEntity;
import com.github.imdabigboss.easydatapack.api.exceptions.*;
import com.github.imdabigboss.easydatapack.api.items.CustomItem;
import org.bukkit.inventory.Recipe;

import java.util.function.Consumer;

/**
 * This class is used to register custom components. It is to be set as an argument in a function that is passed to
 * {@link EasyDatapackAPI#registerCustomAdder(Consumer)}.
 */
public interface CustomAdder {
    /**
     * Registers a custom block.
     * @param block the block to register
     */
    void register(CustomBlock block);

    /**
     * Registers a custom dimension.
     * @param dimension the dimension to register
     * @throws CustomDimensionException if something goes wrong while registering the dimension
     */
    void register(CustomDimension dimension) throws CustomDimensionException;

    /**
     * Registers a custom enchantment.
     * @param enchantment the enchantment to register
     * @throws CustomEnchantmentException if something goes wrong while registering the enchantment
     */
    void register(CustomEnchantment enchantment) throws CustomEnchantmentException;

    /**
     * Registers a custom item.
     * @param item the item to register
     * @throws CustomItemException if something goes wrong while registering the item
     */
    void register(CustomItem item) throws CustomItemException;

    /**
     * Registers a custom recipe.
     * @param recipe the recipe to register
     * @throws CustomRecipeException if something goes wrong while registering the recipe
     */
    void register(Recipe recipe) throws CustomRecipeException;

    /**
     * Registers a custom entity.
     * @param entity the entity to register
     * @throws CustomEntityException if something goes wrong while registering the entity
     */
    void register(CustomEntity entity) throws CustomEntityException;
}
