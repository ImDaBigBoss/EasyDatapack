package com.github.imdabigboss.easydatapack.backend;

import com.github.imdabigboss.easydatapack.api.CustomAdder;
import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.EasyDatapackBase;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
import com.github.imdabigboss.easydatapack.api.utils.PacketUtil;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;
import com.github.imdabigboss.easydatapack.backend.managers.*;
import com.github.imdabigboss.easydatapack.backend.registrar.DefaultRegistrar;
import com.github.imdabigboss.easydatapack.backend.registrar.GenericRegistrar;
import com.github.imdabigboss.easydatapack.backend.registrar.GeyserRegistrar;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import com.github.imdabigboss.easydatapack.backend.utils.YmlConfigImpl;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.GeyserApi;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class EasyDatapack extends JavaPlugin implements EasyDatapackBase {
    private Logger log;
    private YmlConfig config;
    private PacketUtil packetUtil;

    private RecipeManagerImpl recipeManager;
    private BlockManagerImpl blockManager;
    private DimensionManagerImpl dimensionManager;
    private EnchantmentManagerImpl enchantmentManager;
    private ItemManagerImpl itemManager;
    private MapManagerImpl mapManager;
    private EntityManagerImpl entityManager;

    private GenericRegistrar componentRegistrar = null;

    private Map<Class<? extends GenericBuilder<?>>, Class<? extends GenericBuilderImpl>> builders = new HashMap<>();

    @Override
    public void onLoad() {
        EasyDatapackAPI.set(this);

        this.log = this.getLogger();
        this.config = new YmlConfigImpl(this);
        this.config.saveConfig();
        this.packetUtil = new PacketUtil();

        this.recipeManager = new RecipeManagerImpl(this);
        this.blockManager = new BlockManagerImpl(this);
        this.dimensionManager = new DimensionManagerImpl(this);
        this.enchantmentManager = new EnchantmentManagerImpl(this);
        this.itemManager = new ItemManagerImpl(this);
        this.mapManager = new MapManagerImpl(this);
        this.entityManager = new EntityManagerImpl(this);

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
        this.blockManager.registerListener();
        this.dimensionManager.registerListener();
        this.enchantmentManager.registerListener();
        this.itemManager.registerListener();
        this.mapManager.registerListener();
        this.entityManager.registerListener();

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
        this.componentRegistrar.registerCustomAdder(customAdder);
    }

    @Override
    public @NonNull YmlConfig getAPIConfig() {
        return this.config;
    }

    @Override
    public @NonNull PacketUtil getPacketUtil() {
        return this.packetUtil;
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

    public void registerBuilder(@NotNull Class<? extends GenericBuilder<?>> type, @NonNull Class<? extends GenericBuilderImpl> builder) {
        if (this.builders.containsKey(type)) {
            throw new RuntimeException("Builder type " + type + " is already registered.");
        }

        this.builders.put(type, builder);
    }

    @Override
    public @NonNull GenericBuilder<?> createBuilder(@NotNull Class<? extends GenericBuilder<?>> type, @NotNull @NonNull Object... args) {
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
}
