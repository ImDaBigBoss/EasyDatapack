package com.github.imdabigboss.easydatapack.api.types.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * This class represents a custom item that is a block placer. There is no need to create one for your blocks, it is
 * done automatically when a custom block is created.
 */
public interface CustomBlockPlacerItem extends CustomItem {
    /**
     * Creates a new item builder.
     * @param customModelData the custom model data of the item
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     * @return the builder
     */
    static BlockPlacerBuilder builder(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial) {
        return (BlockPlacerBuilder) EasyDatapackAPI.createBuilder(BlockPlacerBuilder.class, customModelData, namespaceKey, name, baseMaterial);
    }

    /**
     * Creates a new item builder.
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     * @param texture the path to the item texture to register
     * @return the builder
     */
    static BlockPlacerBuilder builder(@NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, @NonNull Path texture) {
        int customModelData = EasyDatapackAPI.getTexturePackManager().registerItemTexture(namespaceKey, baseMaterial, false, true, texture);
        return builder(customModelData, namespaceKey, name, baseMaterial);
    }

    /**
     * Gets the event that is called when a player places a block with the item.
     * @return the event that is called when a player places a block with the item
     */
    @Nullable Consumer<BlockPlaceEvent> getBlockPlaceEvent();

    /**
     * This class represents a builder for a custom block placer item.
     */
    interface BlockPlacerBuilder extends Builder {
        /**
         * Sets the event that is called when a player places a block with the item.
         * @param blockPlaceEvent the event that is called when a player places a block with the item
         * @return the builder
         */
        @NonNull BlockPlacerBuilder blockPlaceEvent(@Nullable Consumer<BlockPlaceEvent> blockPlaceEvent);
    }
}
