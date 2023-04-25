package com.github.imdabigboss.easydatapack.api.types.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;

/**
 * This class represents a custom item that is food
 */
public interface CustomFoodItem extends CustomItem {
    /**
     * Creates a new item builder.
     * @param customModelData the custom model data of the item
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     * @return the builder
     */
    static CustomFoodItem.FoodBuilder builder(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, int nutrition, float saturation) {
        return (CustomFoodItem.FoodBuilder) EasyDatapackAPI.createBuilder(FoodBuilder.class, customModelData, namespaceKey, name, baseMaterial, nutrition, saturation);
    }

    /**
     * Creates a new item builder and registers the textures to the texture pack.
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     * @param texture the path to the item texture to register
     * @return the builder
     */
    static CustomFoodItem.FoodBuilder builder(@NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, @NonNull Path texture, int nutrition, float saturation) {
        int customModelData = EasyDatapackAPI.getTexturePackManager().registerItemTexture(baseMaterial, texture);
        return builder(customModelData, namespaceKey, name, baseMaterial, nutrition, saturation);
    }

    /**
     * Gets the nutrition of the food.
     * @return the nutrition of the food
     */
    int getNutrition();

    /**
     * Gets the saturation of the food.
     * @return the saturation of the food
     */
    float getSaturation();

    /**
     * Gets the residue of the food when eaten. This is the item that is left behind after consumption.
     * @return the residue of the food when eaten
     */
    @Nullable Material getResidue();

    /**
     * This class represents a builder for a custom hat item.
     */
    interface FoodBuilder extends Builder {
        /**
         * Sets the residue of the food when eaten. This is the item that is left behind after consumption.
         * @param residue the residue of the food when eaten
         * @return the builder
         */
        @NonNull FoodBuilder residue(@Nullable Material residue);
    }
}
