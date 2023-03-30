package com.github.imdabigboss.easydatapack.api.blocks;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This class represents a custom block.
 */
public interface CustomBlock {
    /**
     * Creates a new custom block builder.
     * @param name the name of the block
     * @param namespaceKey the namespace key of the block
     * @param customModelData the custom model data of the block (used for the item texture in the inventory)
     * @param up up state
     * @param down down state
     * @param north north state
     * @param east east state
     * @param south south state
     * @param west west state
     * @param parent the parent mushroom stem
     */
    static Builder builder(@NonNull String name, @NonNull String namespaceKey, int customModelData, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, @NonNull Parent parent) {
        return (Builder) EasyDatapackAPI.createBuilder(Builder.class, name, namespaceKey, customModelData, up, down, north, east, south, west, parent);
    }

    /**
     * Gets the name of the block.
     * @return the name of the block
     */
    @NonNull String getName();

    /**
     * Gets the namespace key of the block.
     * @return the namespace key of the block
     */
    @NonNull String getNamespaceKey();

    /**
     * Gets the custom model data of the block's item.
     * @return the custom model data of the block's item
     */
    int getCustomModelData();

    /**
     * Gets the up state of the block.
     * @return the up state of the block
     */
    boolean isUp();

    /**
     * Gets the down state of the block.
     * @return the down state of the block
     */
    boolean isDown();

    /**
     * Gets the north state of the block.
     * @return the north state of the block
     */
    boolean isNorth();

    /**
     * Gets the east state of the block.
     * @return the east state of the block
     */
    boolean isSouth();

    /**
     * Gets the east state of the block.
     * @return the east state of the block
     */
    boolean isEast();

    /**
     * Gets the west state of the block.
     * @return the west state of the block
     */
    boolean isWest();

    /**
     * Gets the parent of the block.
     * @return the parent of the block
     */
    @NonNull Parent getParent();

    /**
     * Gets the dropped experience of the block.
     * @return the dropped experience of the block
     */
    int getDropExperience();

    /**
     * Gets the dropped material of the block.
     * @return the dropped material of the block
     */
    @Nullable Material getDropMaterial();

    /**
     * Gets the dropped amount of the block.
     * @return the dropped amount of the block
     */
    int getDropAmount();

    /**
     * Creates the item used to place the block.
     * @return the item used to place the block
     */
    @NonNull ItemStack createBlockItem();

    /**
     * Creates a drop for the block.
     * @return the drop for the block
     */
    @NonNull ItemStack createDrop();

    /**
     * Gets the base block material.
     * @return the base block material
     */
    @NonNull Material getMaterial();

    /**
     * This enum represents the parent of a custom block, the block type used to display our custom block.
     */
    enum Parent {
        MUSHROOM_STEM,
        BROWN_MUSHROOM,
        RED_MUSHROOM
    }

    /**
     * This class represents a custom block builder.
     */
    interface Builder extends GenericBuilder<CustomBlock> {
        /**
         * Sets the drop material of the block.
         * @param dropMaterial the drop material of the block, set to null to drop the block itself
         * @return the builder
         */
        @NonNull Builder dropMaterial(@Nullable Material dropMaterial);

        /**
         * Sets the drop amount of the block.
         * @param dropAmount the drop amount of the block
         * @return the builder
         */
        @NonNull Builder dropAmount(int dropAmount);
        /**
         * Sets the drop experience of the block.
         * @param dropExperience the drop experience of the block
         * @return the builder
         */
        @NonNull Builder dropExperience(int dropExperience);
    }
}
