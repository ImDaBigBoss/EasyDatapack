package com.github.imdabigboss.easydatapack.backend;

import com.github.imdabigboss.easydatapack.api.CustomAdder;
import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.EasyDatapackBase;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;
import com.github.imdabigboss.easydatapack.backend.managers.*;
import com.github.imdabigboss.easydatapack.backend.registrar.*;
import com.github.imdabigboss.easydatapack.backend.utils.YmlConfigImpl;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.GeyserApi;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class EasyDatapack extends JavaPlugin implements EasyDatapackBase {
    private Logger log;
    private YmlConfig config;

    private RecipeManagerImpl recipeManager;
    private BlockManagerImpl blockManager;
    private DimensionManagerImpl dimensionManager;
    private EnchantmentManagerImpl enchantmentManager;
    private ItemManagerImpl itemManager;
    private MapManagerImpl mapManager;

    private GenericRegistrar componentRegistrar = null;

    @Override
    public void onLoad() {
        EasyDatapackAPI.set(this);

        this.log = this.getLogger();
        this.config = new YmlConfigImpl(this);
        this.config.saveConfig();

        this.recipeManager = new RecipeManagerImpl(this);
        this.blockManager = new BlockManagerImpl(this);
        this.dimensionManager = new DimensionManagerImpl(this);
        this.enchantmentManager = new EnchantmentManagerImpl(this);
        this.itemManager = new ItemManagerImpl(this);
        this.mapManager = new MapManagerImpl(this);

        if (this.getServer().getPluginManager().getPlugin("Geyser-Spigot") != null) {
            try {
                GeyserApi geyserApi = GeyserApi.api();
                this.componentRegistrar = new GeyserRegistrar(this, geyserApi);
                this.log.info("Detected Geyser, items will be registered with it.");
            } catch (Exception e) {
                this.componentRegistrar = null;
            }
        }

        if (this.componentRegistrar == null) {
            this.componentRegistrar = new DefaultRegistrar(this);
        }
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(this.blockManager, this);
        pluginManager.registerEvents(this.dimensionManager, this);
        pluginManager.registerEvents(this.enchantmentManager, this);
        pluginManager.registerEvents(this.itemManager, this);
        pluginManager.registerEvents(this.mapManager, this);

        this.componentRegistrar.onEnable();
        log.info("EasyDatapack successfully enabled.");
    }

    @Override
    public void onDisable() {
        this.componentRegistrar.onDisable();
        this.log.info("EasyDatapack successfully disabled.");
    }

    @Override
    public void registerCustomAdder(@NonNull Consumer<CustomAdder> customAdder) {
        this.componentRegistrar.registerCustomAdder(customAdder);
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
