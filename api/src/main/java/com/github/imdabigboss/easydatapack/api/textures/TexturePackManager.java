package com.github.imdabigboss.easydatapack.api.textures;

import com.github.imdabigboss.easydatapack.api.types.blocks.CustomBlock;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;
import java.util.function.Function;

public interface TexturePackManager {
    /**
     * Enables the texture pack creation if not already enabled. This function takes a consumer that will be called when
     * the texture pack is ready to be uploaded to a web server. The pack manager expects the consumer to upload the
     * texture pack to a web server and return the URL to the texture pack.
     * The webUpload can be set to null, in which case another registrar will need to enable it and pass one else an
     * IllegalStateException will be thrown at generation time.
     * The last registrar to enable the texture pack will be the one to upload it to the web server.
     * @param webUpload the consumer to upload the texture pack to a web server
     */
    void enableTexturePack(@Nullable Function<Path, String> webUpload);

    /**
     * Gets whether the texture pack is enabled.
     * @return whether the texture pack is enabled
     */
    boolean isTexturePackEnabled();

    /**
     * Sets whether the texture pack is required by the client.
     * @param required whether the texture pack is required
     */
    void setRequired(boolean required);

    /**
     * Adds a pack to the list of packs to merge into the final texture pack. The last pack added will have authority
     * over what ends up in the pack. Files will only be overwritten.
     * @param pack the .zip file of the pack to merge
     */
    void mergePack(Path pack);

    /**
     * Registers a custom item texture for the given material in the texture pack.
     * @param material the material to register the texture for
     * @param handheld whether the item is held like a tool
     * @param texture the path to the texture to register
     * @return the custom model data of the item
     */
    int registerItemTexture(@NonNull Material material, boolean handheld, boolean blockPlacer, @Nullable Path texture);

    default int registerItemTexture(@NonNull Material material, @Nullable Path texture) {
        return registerItemTexture(material, false, false, texture);
    }

    /**
     * Reserves a custom model data ID for the given material in the texture pack.
     * @param material the material to reserve the custom model data for
     * @return the custom model data of the reserved item
     */
    int reserveItemCMD(@NonNull Material material);

    /**
     * Registers a custom block texture for the given material in the texture pack.
     * @param texture the path to the texture to register
     * @param itemTexture the path to the block item texture to register
     * @return the block data of the block
     */
    BlockData registerBlockTexture(@Nullable Path texture, @Nullable Path itemTexture);

    /**
     * Reserves a block state that can be used for a custom block registration.
     * @return the block data of the reserved block
     */
    BlockData reserveBlockstate();

    /**
     * Block data for a custom block registration
     * @param up up state
     * @param down down state
     * @param north north state
     * @param east east state
     * @param south south state
     * @param west west state
     * @param parent the parent mushroom stem type
     */
    record BlockData(boolean up, boolean down, boolean north, boolean east, boolean south, boolean west, CustomBlock.@NonNull Parent parent) {
    }
}
