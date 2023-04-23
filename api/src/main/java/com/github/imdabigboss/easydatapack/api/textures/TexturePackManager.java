package com.github.imdabigboss.easydatapack.api.textures;

import com.github.imdabigboss.easydatapack.api.blocks.CustomBlock;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

public interface TexturePackManager {
    /**
     * Disables the texture pack creation.
     */
    void disableTexturePack();

    /**
     * Registers a custom item texture for the given material in the texture pack.
     * @param material the material to register the texture for
     * @param texture the path to the texture to register
     * @return the custom model data of the item
     */
    int registerItemTexture(@NonNull Material material, @NonNull Path texture);

    /**
     * Registers a custom block texture for the given material in the texture pack.
     * @param texture the path to the texture to register
     * @param itemTexture the path to the block item texture to register
     * @return the block data of the block
     */
    BlockData registerBlockTexture(@NonNull Path texture, @NonNull Path itemTexture);

    /**
     * Block data for a custom block registration
     * @param customModelData the custom model data of the block
     * @param up up state
     * @param down down state
     * @param north north state
     * @param east east state
     * @param south south state
     * @param west west state
     * @param parent the parent mushroom stem type
     */
    record BlockData(int customModelData, boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, CustomBlock.@NonNull Parent parent) {
    }
}
