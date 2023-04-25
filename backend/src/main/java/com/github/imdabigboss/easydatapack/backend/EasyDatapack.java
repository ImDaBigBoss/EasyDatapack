package com.github.imdabigboss.easydatapack.backend;

import com.github.imdabigboss.easydatapack.api.CustomAdder;
import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.EasyDatapackBase;
import com.github.imdabigboss.easydatapack.api.textures.TexturePackManager;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;
import com.github.imdabigboss.easydatapack.backend.registration.DefaultRegistrar;
import com.github.imdabigboss.easydatapack.backend.registration.GenericRegistrar;
import com.github.imdabigboss.easydatapack.backend.registration.GeyserRegistrar;
import com.github.imdabigboss.easydatapack.backend.textures.OverridableBlockstates;
import com.github.imdabigboss.easydatapack.backend.textures.TexturePackManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.blocks.BlockManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.dimensions.DimensionManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.enchantments.EnchantmentManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.entities.EntityManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.items.ItemManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.maps.MapManagerImpl;
import com.github.imdabigboss.easydatapack.backend.types.recipies.RecipeManagerImpl;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import com.github.imdabigboss.easydatapack.backend.utils.YmlConfigImpl;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.GeyserApi;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
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
    private EntityManagerImpl entityManager;
    private TexturePackManagerImpl texturePackManager;

    private GenericRegistrar componentRegistrar = null;

    private final Map<Class<? extends GenericBuilder<?>>, Class<? extends GenericBuilderImpl>> builders = new HashMap<>();

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
        this.entityManager = new EntityManagerImpl(this);
        this.texturePackManager = new TexturePackManagerImpl(this);

        if (this.getServer().getPluginManager().getPlugin("Geyser-Spigot") != null) {
            try {
                GeyserApi geyserApi = GeyserApi.api();
                this.log.info("Detected Geyser, items will be registered with it.");
                this.componentRegistrar = new GeyserRegistrar(this, geyserApi);
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
        this.blockManager.registerListener();
        this.dimensionManager.registerListener();
        this.enchantmentManager.registerListener();
        this.itemManager.registerListener();
        this.mapManager.registerListener();
        this.entityManager.registerListener();
        this.texturePackManager.registerListener();

        this.componentRegistrar.onEnable();
        this.log.info("EasyDatapack successfully enabled.");
    }

    @Override
    public void onDisable() {
        this.componentRegistrar.onDisable();
        this.log.info("EasyDatapack successfully disabled.");
    }

    @Override
    public void registerCustomAdder(@NonNull Consumer<CustomAdder> customAdder) {
        if (this.componentRegistrar.isFinalized()) {
            throw new IllegalStateException("Custom adders can no longer be registered, the plugin has already registered all the custom components");
        }

        this.componentRegistrar.registerCustomAdder(customAdder);
    }

    public void registrationComplete() {
        this.texturePackManager.registrationComplete();
        OverridableBlockstates.garbageCollect();
    }

    @Override
    public @NonNull YmlConfig getAPIConfig() {
        return this.config;
    }

    public void registerBuilder(@NonNull Class<? extends GenericBuilder<?>> type, @NonNull Class<? extends GenericBuilderImpl> builder) {
        if (this.builders.containsKey(type)) {
            throw new RuntimeException("Builder type " + type + " is already registered.");
        }

        this.builders.put(type, builder);
    }

    @Override
    public @NonNull GenericBuilder<?> createBuilder(@NonNull Class<? extends GenericBuilder<?>> type, @NonNull Object... args) {
        Class<? extends GenericBuilderImpl> builder = this.builders.get(type);
        if (builder == null) {
            throw new RuntimeException("Builder type " + type + " hasn't been registered... Please speak to the developer.");
        }

        try {
            Constructor<?>[] constructors = builder.getConstructors();
            if (constructors.length == 0) {
                throw new RuntimeException("Builder type " + builder.getName() + " has no constructor.");
            } else if (constructors.length > 1) {
                throw new RuntimeException("Builder type " + builder.getName() + " has more than one constructor.");
            }

            Constructor<?> constructor = constructors[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != args.length) {
                throw new RuntimeException("Builder type " + builder.getName() + " has a constructor with " + parameterTypes.length + " parameters, but " + args.length + " were provided.");
            }

            return (GenericBuilder<?>) constructor.newInstance(args);
        } catch (SecurityException |  InstantiationException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e) {
            throw new RuntimeException("Could not create builder of type " + builder.getName(), e);
        }
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

    @Override
    public @NonNull EntityManagerImpl getEntityManager() {
        return this.entityManager;
    }

    @Override
    public @NonNull TexturePackManager getTexturePackManager() {
        return this.texturePackManager;
    }
}
