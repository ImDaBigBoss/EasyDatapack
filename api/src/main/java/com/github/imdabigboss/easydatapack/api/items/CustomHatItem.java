package com.github.imdabigboss.easydatapack.api.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import org.bukkit.Material;

/**
 * This class represents a custom item that is a hat, and thus can be worn.
 */
public interface CustomHatItem extends CustomItem {
    /**
     * Creates a new item builder.
     * @param customModelData the custom model data of the item
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     */
    static CustomHatItem.HatBuilder builder(int customModelData, String namespaceKey, String name, Material baseMaterial) {
        return (CustomHatItem.HatBuilder) EasyDatapackAPI.createBuilder(HatBuilder.class, customModelData, namespaceKey, name, baseMaterial);
    }

    /**
     * This class represents a builder for a custom hat item.
     */
    interface HatBuilder extends Builder {

    }
}
