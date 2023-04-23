package com.github.imdabigboss.easydatapack.backend.types.entities;

import com.github.imdabigboss.easydatapack.api.entities.CustomEntity;
import com.github.imdabigboss.easydatapack.api.entities.CustomEntityGoal;
import com.github.imdabigboss.easydatapack.api.entities.model.EntityModel;
import com.github.imdabigboss.easydatapack.backend.types.entities.model.EntityModelImpl;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CustomEntityImpl implements CustomEntity {
    private final String name;
    private final String namespaceKey;
    private final int eggCustomModelData;
    private final EntityModelImpl model;
    private final Map<Integer, CustomEntityGoal> goals;
    private final int health;

    private CustomEntityImpl(@NonNull String name, @NonNull String namespaceKey, int eggCustomModelData, @NonNull EntityModelImpl model, @NonNull Map<Integer, CustomEntityGoal> goals, int health) {
        this.name = name;
        this.namespaceKey = namespaceKey;
        this.eggCustomModelData = eggCustomModelData;
        this.model = model;
        this.goals = goals;
        this.health = health;
    }

    @Override
    public @NonNull String getName() {
        return this.name;
    }

    @Override
    public @NonNull String getNamespaceKey() {
        return this.namespaceKey;
    }

    @Override
    public int getEggCustomModelData() {
        return this.eggCustomModelData;
    }

    @Override
    public @NonNull EntityModelImpl getModel() {
        return this.model;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public @NonNull ItemStack getSpawnEgg() {
        ItemStack egg = new ItemStack(Material.SKELETON_SPAWN_EGG);

        ItemMeta meta = egg.getItemMeta();
        meta.displayName(Component.text(ChatColor.RESET + "Spawn " + this.getName()));
        meta.setCustomModelData(this.eggCustomModelData);
        egg.setItemMeta(meta);

        return egg;
    }

    @Override
    public @NonNull Map<Integer, CustomEntityGoal> getGoals() {
        return new HashMap<>(this.goals);
    }

    public EntityModelImpl spawn(Entity parent, Location location) {
        EntityModelImpl newModel = this.model.clone();
        newModel.spawn(parent, location);
        return newModel;
    }

    public static class BuilderImpl implements Builder, GenericBuilderImpl {
        protected final String name;
        protected final String namespaceKey;
        protected final int eggCustomModelData;
        protected final EntityModelImpl model;
        protected final Map<Integer, CustomEntityGoal> goals = new HashMap<>();

        protected int health = 20;

        public BuilderImpl(String name, String namespaceKey, int eggCustomModelData, EntityModel model) {
            this.name = name;
            this.namespaceKey = namespaceKey;
            this.eggCustomModelData = eggCustomModelData;
            this.model = (EntityModelImpl) model;
        }

        @Override
        public Builder addGoal(int priority, CustomEntityGoal goal) {
            this.goals.put(priority, goal);
            return this;
        }

        @Override
        public Builder setHealth(int health) {
            this.health = health;
            return this;
        }

        @Override
        public @NonNull CustomEntityImpl build() {
            return new CustomEntityImpl(this.name, this.namespaceKey, this.eggCustomModelData, this.model, this.goals, this.health);
        }
    }
}
