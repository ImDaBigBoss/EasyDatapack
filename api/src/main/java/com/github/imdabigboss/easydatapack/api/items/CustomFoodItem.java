package com.github.imdabigboss.easydatapack.api.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

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
     */
    static CustomFoodItem.FoodBuilder builder(int customModelData, String namespaceKey, String name, Material baseMaterial, int nutrition, float saturation) {
        return (CustomFoodItem.FoodBuilder) EasyDatapackAPI.createBuilder(FoodBuilder.class, customModelData, namespaceKey, name, baseMaterial, nutrition, saturation);
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
        FoodBuilder residue(@Nullable Material residue);
    }
}
