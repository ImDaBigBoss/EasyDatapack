package com.github.imdabigboss.easydatapack.backend.registration;

import com.github.imdabigboss.easydatapack.api.CustomAdder;
import com.github.imdabigboss.easydatapack.api.exceptions.*;
import com.github.imdabigboss.easydatapack.api.types.blocks.CustomBlock;
import com.github.imdabigboss.easydatapack.api.types.dimentions.CustomDimension;
import com.github.imdabigboss.easydatapack.api.types.enchantments.CustomEnchantment;
import com.github.imdabigboss.easydatapack.api.types.entities.CustomEntity;
import com.github.imdabigboss.easydatapack.api.types.items.CustomItem;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import com.github.imdabigboss.easydatapack.backend.types.blocks.BlockManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.dimensions.DimensionManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.enchantments.EnchantmentManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.entities.EntityManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.items.ItemManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.recipies.RecipeManagerImpl;
import org.bukkit.inventory.Recipe;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GenericRegistrar implements CustomAdder {
    private final List<Consumer<CustomAdder>> customAdders = new ArrayList<>();
    private boolean finalized = false;
    protected final EasyDatapack datapack;

    protected final BlockManagerImpl blockManager;
    protected final DimensionManagerImpl dimensionManager;
    protected final EnchantmentManagerImpl enchantmentManager;
    protected final ItemManagerImpl itemManager;
    protected final RecipeManagerImpl recipeManager;
    protected final EntityManagerImpl entityManager;

    public GenericRegistrar(EasyDatapack datapack) {
        this.datapack = datapack;

        this.blockManager = datapack.getBlockManager();
        this.dimensionManager = datapack.getDimensionManager();
        this.enchantmentManager = datapack.getEnchantmentManager();
        this.itemManager = datapack.getItemManager();
        this.recipeManager = datapack.getRecipeManager();
        this.entityManager = datapack.getEntityManager();
    }

    public void onEnable() {
        this.register();
    }

    protected void register() {
    }

    public void onDisable() {
        this.datapack.getRecipeManager().unregisterAllRecipes();
        this.datapack.getEnchantmentManager().unregisterEnchantments();

        this.unregister();
    }

    protected void unregister() {
    }

    public void registerCustomAdder(@NonNull Consumer<CustomAdder> customAdder) {
        this.customAdders.add(customAdder);
    }

    protected void registerCustomComponents() {
        for (Consumer<CustomAdder> customAdderConsumer : this.customAdders) {
            customAdderConsumer.accept(this);
        }
    }

    protected void finalizeRegistration() {
        this.datapack.getEnchantmentManager().registerEventListeners();
        this.datapack.getItemManager().registerEventListeners();

        this.datapack.getLogger().info("Custom component registration complete!");

        if (this.datapack.getDimensionManager().getCustomDimensions().size() > 0) {
            this.datapack.getLogger().info("Creating custom dimension worlds...");
            this.datapack.getDimensionManager().createWorlds();
        }

        this.finalized = true;
        this.datapack.registrationComplete();
    }

    public boolean isFinalized() {
        return this.finalized;
    }

    @Override
    public void register(CustomBlock block) {
        this.blockManager.registerCustomBlock(block);
        this.blockRegistered(block);
    }

    protected void blockRegistered(CustomBlock block) {
    }

    @Override
    public void register(CustomDimension dimension) throws CustomDimensionException {
        this.dimensionManager.registerCustomDimension(dimension);
        this.dimensionRegistered(dimension);
    }

    protected void dimensionRegistered(CustomDimension dimension) {
    }

    @Override
    public void register(CustomEnchantment enchantment) throws CustomEnchantmentException {
        this.enchantmentManager.registerCustomEnchantment(enchantment);
        this.enchantmentRegistered(enchantment);
    }

    protected void enchantmentRegistered(CustomEnchantment enchantment) {
    }

    @Override
    public void register(CustomItem item) throws CustomItemException {
        this.itemManager.registerCustomItem(item);
        this.itemRegistered(item);
    }

    protected void itemRegistered(CustomItem item) {
    }

    @Override
    public void register(Recipe recipe) throws CustomRecipeException {
        this.recipeManager.registerCustomRecipe(recipe);
        this.recipeRegistered(recipe);
    }

    protected void recipeRegistered(Recipe recipe) {
    }

    @Override
    public void register(CustomEntity entity) throws CustomEntityException {
        this.entityManager.registerCustomEntity(entity);
        this.entityRegistered(entity);
    }

    protected void entityRegistered(CustomEntity entity) {
    }
}
