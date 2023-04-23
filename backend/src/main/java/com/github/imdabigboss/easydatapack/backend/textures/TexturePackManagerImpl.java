package com.github.imdabigboss.easydatapack.backend.textures;

import com.github.imdabigboss.easydatapack.api.textures.TexturePackManager;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TexturePackManagerImpl implements TexturePackManager {
    private final EasyDatapack datapack;

    private boolean enabled = true;
    private boolean allowRegistering = true;
    private final Map<Material, List<Path>> itemTextures = new HashMap<>();
    private final Map<Integer, List<Path>> blockTextures = new HashMap<>();

    public TexturePackManagerImpl(EasyDatapack datapack) {
        this.datapack = datapack;
        OverridableBlockstates.init();
    }

    @Override
    public void disableTexturePack() {
        this.enabled = false;
        this.datapack.getLogger().info("Texture pack creation disabled.");
    }

    public void registrationComplete() {
        this.allowRegistering = false;

        if (!this.enabled) {
            return;
        }

        //TODO: Create texture pack
    }

    @Override
    public int registerItemTexture(@NonNull Material material, @NonNull Path texture) {
        if (!this.allowRegistering) {
            throw new IllegalStateException("Cannot register textures after registration is complete.");
        }

        if (!this.itemTextures.containsKey(material)) {
            this.itemTextures.put(material, new ArrayList<>());
        }

        int cmd = this.itemTextures.get(material).size() + 1;

        this.itemTextures.get(material).add(texture);

        return cmd;
    }

    @Override
    public BlockData registerBlockTexture(@NonNull Path texture, @NonNull Path itemTexture) {
        if (!this.allowRegistering) {
            throw new IllegalStateException("Cannot register textures after registration is complete.");
        }

        int cmd = this.registerItemTexture(Material.CLOCK, itemTexture);

        if (!this.blockTextures.containsKey(cmd)) {
            this.blockTextures.put(cmd, new ArrayList<>());
        }

        OverridableBlockstates.Blockstate blockstate = OverridableBlockstates.availableStates.get(this.blockTextures.get(cmd).size() + 1);
        this.blockTextures.get(cmd).add(texture);

        return new BlockData(cmd, blockstate.up(), blockstate.down(), blockstate.north(), blockstate.east(), blockstate.south(), blockstate.west(), blockstate.parent());
    }
}
