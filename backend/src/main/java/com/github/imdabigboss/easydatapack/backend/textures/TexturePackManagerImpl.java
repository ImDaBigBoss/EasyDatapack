package com.github.imdabigboss.easydatapack.backend.textures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.imdabigboss.easydatapack.api.exceptions.EasyDatapackException;
import com.github.imdabigboss.easydatapack.api.textures.TexturePackManager;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import com.github.imdabigboss.easydatapack.backend.utils.FileUtils;
import com.github.imdabigboss.easydatapack.backend.utils.GenericManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;

public class TexturePackManagerImpl extends GenericManager implements TexturePackManager {
    private boolean enabled = false;
    private boolean allowRegistering = true;
    private boolean required = false;

    private Function<Path, String> webUpload = null;
    private String texturePackUrl = null;
    private byte[] texturePackHash = null;

    private final List<Path> mergePacks = new ArrayList<>();

    private final Map<Material, Integer> itemCounts = new HashMap<>();
    private final List<ItemData> itemData = new ArrayList<>();
    private final List<BlockData> blockData = new ArrayList<>();

    public TexturePackManagerImpl(EasyDatapack datapack) {
        super(datapack);
        OverridableBlockstates.init();
    }

    @Override
    public void registerBuilders() {
        //There are no builders to register
    }

    @Override
    public void enableTexturePack(@Nullable Function<Path, String> webUpload) {
        this.enabled = true;
        this.webUpload = webUpload;
    }

    @Override
    public boolean isTexturePackEnabled() {
        return false;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void mergePack(Path pack) {
        this.mergePacks.add(pack);
    }

    private void checkRegistration() {
        if (!this.allowRegistering) {
            throw new IllegalStateException("Cannot register textures after registration is complete.");
        }
        if (!this.enabled) {
            throw new IllegalStateException("Cannot register textures when texture pack creation is disabled.");
        }
    }

    @Override
    public int registerItemTexture(@NonNull Material material, boolean handheld, boolean blockPlacer, @Nullable Path texture) {
        this.checkRegistration();

        int cmd;
        if (!this.itemCounts.containsKey(material)) {
            cmd = 1;
            this.itemCounts.put(material, 1);
        } else {
            cmd = this.itemCounts.get(material) + 1;
            this.itemCounts.replace(material, cmd);
        }

        if (cmd > 9999999) {
            this.itemCounts.replace(material, 9999999);
            throw new IllegalStateException("Cannot register more than 9999999 textures for a single item.");
        }

        this.itemData.add(new ItemData(cmd, material, handheld, blockPlacer, texture));

        return cmd;
    }

    @Override
    public int reserveItemCMD(@NonNull Material material) {
        return this.registerItemTexture(material, null);
    }

    @Override
    public TexturePackManager.BlockData registerBlockTexture(@Nullable Path texture, @Nullable Path itemTexture) {
        this.checkRegistration();

        int blockId = this.blockData.size() + 1;
        if (blockId > OverridableBlockstates.availableStates.size()) {
            throw new IllegalStateException("Cannot register more block textures than available blockstates.");
        }

        OverridableBlockstates.Blockstate blockstate = OverridableBlockstates.availableStates.get(blockId - 1);
        TexturePackManager.BlockData blockData = new TexturePackManager.BlockData(blockstate.up(), blockstate.down(), blockstate.north(), blockstate.east(), blockstate.south(), blockstate.west(), blockstate.parent());

        this.blockData.add(new BlockData(blockData, texture));

        return blockData;
    }

    @Override
    public TexturePackManager.BlockData reserveBlockstate() {
        return this.registerBlockTexture(null, null);
    }

    private void generatePack(Path packFolder) throws IOException, EasyDatapackException {
        if (this.allowRegistering) {
            throw new IllegalStateException("Cannot generate texture pack before registration is complete.");
        }

        Path defaultPack = this.datapack.getDataFolder().toPath().resolve("default_pack.zip");
        if (Files.exists(defaultPack)) {
            Files.delete(defaultPack);
        }
        FileUtils.extractResource(this.datapack, "default_pack.zip", defaultPack);
        this.mergePacks.add(defaultPack);

        for (Path mergePack : this.mergePacks) {
            Path tmp = this.datapack.getDataFolder().toPath().resolve("tmp");
            FileUtils.unzipFile(mergePack, tmp);
            FileUtils.mergeFolders(tmp, packFolder);
            FileUtils.deleteFolder(tmp);
        }

        Files.delete(defaultPack);

        Path defaultModelsZip = this.datapack.getDataFolder().toPath().resolve("default_models.zip");
        Path defaultModels = this.datapack.getDataFolder().toPath().resolve("default_models");
        if (Files.exists(defaultModelsZip)) {
            Files.delete(defaultModelsZip);
        }
        if (Files.exists(defaultModels)) {
            FileUtils.deleteFolder(defaultModels);
        }
        FileUtils.extractResource(this.datapack, "default_models.zip", defaultModelsZip);
        FileUtils.unzipFile(defaultModelsZip, defaultModels);
        Files.delete(defaultModelsZip);

        Path assetPath = packFolder.resolve("assets").resolve("minecraft");

        Path modelsPath = assetPath.resolve("models");
        Files.createDirectories(modelsPath.resolve("item/custom"));
        Files.createDirectories(modelsPath.resolve("block/custom"));

        Path texturesPath = assetPath.resolve("textures");
        Files.createDirectories(texturesPath.resolve("item/custom"));
        Files.createDirectories(texturesPath.resolve("block/custom"));

        ObjectMapper mapper = new ObjectMapper();

        for (ItemData item : this.itemData) {
            if (item.texture() == null) {
                continue;
            }

            String baseNamespaceKey = item.material().toString().toLowerCase(Locale.ROOT);
            String namespaceKey = baseNamespaceKey + "_" + item.customModelData();
            String packPath = "item/custom/" + namespaceKey;

            Path baseModel = modelsPath.resolve("item/" + baseNamespaceKey + ".json");
            if (!Files.exists(baseModel)) {
                Files.copy(defaultModels.resolve("item/" + baseNamespaceKey + ".json"), baseModel);
            }
            ObjectNode root = (ObjectNode) mapper.readTree(baseModel.toFile());
            ArrayNode overrides;
            if (root.has("overrides")) {
                JsonNode node = root.get("overrides");
                if (!node.isArray()) {
                    throw new EasyDatapackException("Overrides node is not an array.");
                }
                overrides = (ArrayNode) node;
                root.remove("overrides");
            } else {
                overrides = mapper.createArrayNode();
            }

            int index = 0;
            for (int i = 0; i < overrides.size(); i++) {
                JsonNode node = overrides.get(i);
                if (!node.isObject()) {
                    throw new EasyDatapackException("Override node is not an object.");
                }
                JsonNode predicate = node.get("predicate");
                if (predicate == null || !predicate.isObject()) {
                    throw new EasyDatapackException("Override predicate node is not an object.");
                }

                JsonNode customModelData = predicate.get("custom_model_data");
                if (customModelData == null) {
                    index++;
                    continue;
                }
                if (!customModelData.isInt()) {
                    throw new EasyDatapackException("Override predicate custom_model_data node is not an integer.");
                }
                int cmd = customModelData.asInt();

                if (cmd == item.customModelData()) {
                    throw new EasyDatapackException("Cannot register two textures with the same custom model data. " + item.material() + ": " + item.customModelData());
                } else if (cmd > item.customModelData()) {
                    break;
                }

                index++;
            }

            ObjectNode override = mapper.createObjectNode();
            ObjectNode predicate = mapper.createObjectNode();
            predicate.put("custom_model_data", item.customModelData());
            override.set("predicate", predicate);
            override.put("model", packPath);
            overrides.insert(index, override);

            root.set("overrides", overrides);
            mapper.writerWithDefaultPrettyPrinter().writeValue(baseModel.toFile(), root);

            Path customModel = modelsPath.resolve(packPath + ".json");
            if (Files.exists(customModel)) {
                Files.delete(customModel);
            }
            String content;
            if (item.blockPlacer()) {
                content = "{\n" +
                        "  \"textures\": {\n" +
                        "    \"0\": \"" + packPath + "\",\n" +
                        "    \"particle\": \"" + packPath + "\"\n" +
                        "  },\n" +
                        "  \"elements\": [\n" +
                        "    {\n" +
                        "      \"from\": [0, 0, 0],\n" +
                        "      \"to\": [16, 16, 16],\n" +
                        "      \"faces\": {\n" +
                        "        \"north\": {\"uv\": [0, 0, 16, 16], \"texture\": \"#0\"},\n" +
                        "        \"east\": {\"uv\": [0, 0, 16, 16], \"texture\": \"#0\"},\n" +
                        "        \"south\": {\"uv\": [0, 0, 16, 16], \"texture\": \"#0\"},\n" +
                        "        \"west\": {\"uv\": [0, 0, 16, 16], \"texture\": \"#0\"},\n" +
                        "        \"up\": {\"uv\": [0, 0, 16, 16], \"texture\": \"#0\"},\n" +
                        "        \"down\": {\"uv\": [0, 0, 16, 16], \"texture\": \"#0\"}\n" +
                        "      }\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"display\": {\n" +
                        "    \"thirdperson_righthand\": {\n" +
                        "      \"rotation\": [0, -43.25, 0],\n" +
                        "      \"scale\": [0.3, 0.3, 0.3]\n" +
                        "    },\n" +
                        "    \"thirdperson_lefthand\": {\n" +
                        "      \"rotation\": [0, -43.25, 0],\n" +
                        "      \"scale\": [0.3, 0.3, 0.3]\n" +
                        "    },\n" +
                        "    \"firstperson_righthand\": {\n" +
                        "      \"rotation\": [0, -43.25, 0],\n" +
                        "      \"translation\": [-0.5, 0.25, 0.5],\n" +
                        "      \"scale\": [0.4, 0.4, 0.4]\n" +
                        "    },\n" +
                        "    \"firstperson_lefthand\": {\n" +
                        "      \"rotation\": [0, -43.25, 0],\n" +
                        "      \"scale\": [0.5, 0.5, 0.5]\n" +
                        "    },\n" +
                        "    \"ground\": {\n" +
                        "      \"scale\": [0.3, 0.3, 0.3]\n" +
                        "    },\n" +
                        "    \"gui\": {\n" +
                        "      \"rotation\": [20, 45, 0],\n" +
                        "      \"scale\": [0.6, 0.6, 0.6]\n" +
                        "    },\n" +
                        "    \"fixed\": {\n" +
                        "      \"rotation\": [-90, 0, 0],\n" +
                        "      \"translation\": [0, 0, -16],\n" +
                        "      \"scale\": [2.01, 2.01, 2.01]\n" +
                        "    }\n" +
                        "  }\n" +
                        "}\n";
            } else {
                content = "{\n" +
                        "  \"parent\": \"" + (item.handheld() ? "item/handheld" : "item/generated") + "\",\n" +
                        "  \"textures\": {\n" +
                        "    \"layer0\": \"" + packPath + "\"\n" +
                        "  }\n" +
                        "}";
            }
            Files.writeString(customModel, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);

            Path customTexture = texturesPath.resolve(packPath + ".png");
            if (Files.exists(customTexture)) {
                Files.delete(customTexture);
            }
            Files.copy(item.texture(), customTexture);
        }

        for (BlockData block : this.blockData) {
            if (block.texture() == null) {
                continue;
            }
        }

        FileUtils.deleteFolder(defaultModels);
    }

    public void registrationComplete() {
        this.allowRegistering = false;

        if (!this.enabled) {
            return;
        }
        if (this.webUpload == null) {
            throw new IllegalStateException("Cannot create texture pack when web upload is not set. The players would be unable to download the texture pack.");
        }

        Path zipPath = this.datapack.getDataFolder().toPath().resolve("pack.zip");
        Path folderPath = this.datapack.getDataFolder().toPath().resolve("pack");

        try {
            if (Files.exists(zipPath)) {
                Files.delete(zipPath);
            }
            if (Files.exists(folderPath)) {
                FileUtils.deleteFolder(folderPath);
            }
            Files.createDirectory(folderPath);
        } catch (Exception e) {
            this.datapack.getLogger().severe("Failed to delete old texture pack generation files. It isn't possible to proceed with texture pack generation.");
            e.printStackTrace();
            this.enabled = false;
            return;
        }

        try {
            this.generatePack(folderPath);
        } catch (Exception e) {
            this.datapack.getLogger().severe("Failed generate texture pack. It isn't possible to proceed with texture pack generation.");
            e.printStackTrace();
            this.enabled = false;
            return;
        }

        try {
            FileUtils.zipFolder(folderPath, zipPath);
            FileUtils.deleteFolder(folderPath);

            this.texturePackHash = FileUtils.sha1Hash(zipPath);
            this.texturePackUrl = this.webUpload.apply(zipPath);
        } catch (Exception e) {
            this.datapack.getLogger().severe("Failed to finalise the texture pack generation.");
            e.printStackTrace();
            this.enabled = false;
            return;
        }

        this.datapack.getLogger().info("Texture pack generation complete. It will be sent to players when they join the server.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!this.enabled) {
            return;
        }

        Player player = event.getPlayer();
        player.setResourcePack(this.texturePackUrl, this.texturePackHash, Component.text("This server recommends the usage of a texture pack. For a better experience, please accept it."), this.required);
    }

    private record ItemData(int customModelData, Material material, boolean handheld, boolean blockPlacer, Path texture) {
    }

    private record BlockData(TexturePackManager.BlockData blockData, Path texture) {
    }
}
