package com.github.imdabigboss.easydatapack.backend;

import com.github.imdabigboss.easydatapack.api.CustomAdder;
import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.EasyDatapackBase;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomRecipeException;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;
import com.github.imdabigboss.easydatapack.backend.managers.*;
import com.github.imdabigboss.easydatapack.backend.utils.YmlConfigImpl;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class EasyDatapack extends JavaPlugin implements EasyDatapackBase {
    private static final Logger log = Logger.getLogger("Minecraft");
    private YmlConfig config;

    private RecipeManagerImpl recipeManager;
    private BlockManagerImpl blockManager;
    private DimensionManagerImpl dimensionManager;
    private EnchantmentManagerImpl enchantmentManager;
    private ItemManagerImpl itemManager;
    private MapManagerImpl mapManager;

    private List<Consumer<CustomAdder>> customAdders = new ArrayList<>();

    @Override
    public void onLoad() {
        EasyDatapackAPI.set(this);

        this.config = new YmlConfigImpl(this);
        this.config.saveConfig();

        this.recipeManager = new RecipeManagerImpl(this);
        this.blockManager = new BlockManagerImpl(this);
        this.dimensionManager = new DimensionManagerImpl(this);
        this.enchantmentManager = new EnchantmentManagerImpl(this);
        this.itemManager = new ItemManagerImpl(this);
        this.mapManager = new MapManagerImpl(this);
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(this.blockManager, this);
        pluginManager.registerEvents(this.dimensionManager, this);
        pluginManager.registerEvents(this.enchantmentManager, this);
        pluginManager.registerEvents(this.itemManager, this);
        pluginManager.registerEvents(this.mapManager, this);

        log.info(String.format("[%s] Registering custom components.", getDescription().getName()));
        CustomAdderImpl customAdder = new CustomAdderImpl(this);
        for (Consumer<CustomAdder> customAdderConsumer : this.customAdders) {
            customAdderConsumer.accept(customAdder);
        }
        this.customAdders = null;

        this.enchantmentManager.registerEventListeners();
        this.itemManager.registerEventListeners();

        log.info(String.format("[%s] Creating custom dimension worlds.", getDescription().getName()));
        this.dimensionManager.createWorlds();

        log.info(String.format("[%s] All done.", getDescription().getName()));
    }

    @Override
    public void onDisable() {
        try {
            this.recipeManager.unregisterAllRecipes();
        } catch (CustomRecipeException e) {
            e.printStackTrace();
        }

        this.enchantmentManager.unregisterEnchantments();
    }

    @Override
    public void registerCustomAdder(@NonNull Consumer<CustomAdder> customAdder) {
        if (this.customAdders == null) {
            throw new RuntimeException("Custom adders can no longer be registered, the plugin has already registered all the custom components");
        }

        this.customAdders.add(customAdder);
    }

    @Override
    public @NonNull YmlConfig getAPIConfig() {
        return this.config;
    }

    @Override
    public @NonNull RecipeManagerImpl getRecipeManager() {
        return recipeManager;
    }

    @Override
    public @NonNull BlockManagerImpl getBlockManager() {
        return blockManager;
    }

    @Override
    public @NonNull DimensionManagerImpl getDimensionManager() {
        return dimensionManager;
    }

    @Override
    public @NonNull EnchantmentManagerImpl getEnchantmentManager() {
        return enchantmentManager;
    }

    @Override
    public @NonNull ItemManagerImpl getItemManager() {
        return itemManager;
    }

    @Override
    public @NonNull MapManagerImpl getMapManager() {
        return mapManager;
    }
}
