package com.github.imdabigboss.easydatapack.api.entities;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.entities.model.EntityModel;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

/**
 * This class represents a custom entity.
 */
public interface CustomEntity {
    /**
     * Creates a custom entity builder.
     * @param name the name of the entity
     * @param namespaceKey the namespace key of the entity
     * @param eggCustomModelData the custom model data of the spawn egg
     */
    static Builder builder(String name, String namespaceKey, int eggCustomModelData, EntityModel model) {
        return (Builder) EasyDatapackAPI.createBuilder(Builder.class, name, namespaceKey, eggCustomModelData, model);
    }

    /**
     * Gets the name of the entity.
     * @return the name of the entity
     */
    @NonNull String getName();

    /**
     * Gets the namespace key of the entity.
     * @return the namespace key of the entity
     */
    @NonNull String getNamespaceKey();

    /**
     * Gets the custom model data of the spawn egg.
     * @return the custom model data of the spawn egg
     */
    int getEggCustomModelData();

    /**
     * Gets the model of the entity.
     * @return the model of the entity
     */
    @NonNull EntityModel getModel();

    /**
     * Gets the maximum health of the entity.
     * @return the maximum health of the entity
     */
    int getHealth();

    /**
     * Creates the spawn egg item stack for the entity.
     * @return the spawn egg of the entity
     */
    @NonNull ItemStack getSpawnEgg();

    /**
     * Gets the goals of the entity.
     * @return the goals of the entity, mapped by priority
     */
    @NonNull Map<Integer, CustomEntityGoal> getGoals();

    /**
     * This class is used to build custom entities.
     */
    interface Builder extends GenericBuilder<CustomEntity> {
        /**
         * Adds a goal to the entity.
         * @param priority the priority of the goal
         * @param goal the goal to add
         * @return the builder
         */
        Builder addGoal(int priority, CustomEntityGoal goal);

        /**
         * Sets the maximum health of the entity.
         * @param health the maximum health of the entity
         * @return the builder
         */
        Builder setHealth(int health);
    }
}
