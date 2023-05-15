package com.github.imdabigboss.easydatapack.api.types.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

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
     * @return the builder
     */
    static HatBuilder builder(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial) {
        return (HatBuilder) EasyDatapackAPI.createBuilder(HatBuilder.class, customModelData, namespaceKey, name, baseMaterial);
    }

    /**
     * Creates a new item builder and registers the textures to the texture pack.
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     * @param texture the path to the item texture to register
     * @return the builder
     */
    static HatBuilder builder(@NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, @NonNull Path texture) {
        int customModelData = EasyDatapackAPI.getTexturePackManager().registerItemTexture(namespaceKey, baseMaterial, texture);
        return  builder(customModelData, namespaceKey, name, baseMaterial);
    }

    /**
     * This class represents a builder for a custom hat item.
     */
    interface HatBuilder extends Builder {

    }
}
