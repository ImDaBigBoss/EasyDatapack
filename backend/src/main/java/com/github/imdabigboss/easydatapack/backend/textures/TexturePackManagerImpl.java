package com.github.imdabigboss.easydatapack.backend.textures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.imdabigboss.easydatapack.api.exceptions.EasyDatapackException;
import com.github.imdabigboss.easydatapack.api.textures.TexturePackManager;
import com.github.imdabigboss.easydatapack.api.utils.YmlConfig;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;
import com.github.imdabigboss.easydatapack.backend.utils.FileUtils;
import com.github.imdabigboss.easydatapack.backend.utils.GenericManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
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
    private final YmlConfig config;

    private boolean enabled = false;
    private boolean allowRegistering = true;
    private boolean required = false;

    private Function<Path, String> webUpload = null;
    private String texturePackUrl = null;
    private byte[] texturePackHash = null;

    private final List<Path> mergePacks = new ArrayList<>();

    private final Map<String, Integer> reservedItemIDs = new HashMap<>();
    private final Map<String, Integer> reservedBlockIDs = new HashMap<>();

    private final Map<Material, Integer> currentItemIDs = new HashMap<>();
    private int currentBlockID = 0;
    private final HashMap<String, ItemData> itemData = new HashMap<>();
    private final HashMap<String, BlockData> blockData = new HashMap<>();

    public TexturePackManagerImpl(EasyDatapack datapack) {
        super(datapack);
        OverridableBlockstates.init();

        this.config = datapack.getAPIConfig();
        MemorySection reserved = (MemorySection) this.config.getConfig().get("reserved.items");
        if (reserved != null) {
            for (String entry : reserved.getKeys(false)) {
                this.reservedItemIDs.put(entry, reserved.getInt(entry));
            }
        }

        reserved = (MemorySection) this.config.getConfig().get("reserved.blocks");
        if (reserved != null) {
            for (String entry : reserved.getKeys(false)) {
                this.reservedBlockIDs.put(entry, reserved.getInt(entry));
            }
        }
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
    public int registerItemTexture(@NonNull String namespaceKey, @NonNull Material material, boolean handheld, boolean blockPlacer, @Nullable Path texture) {
        this.checkRegistration();

        if (this.itemData.containsKey(namespaceKey)) {
            throw new IllegalStateException("Another item has the same namespace key, cannot register the same item texture twice.");
        }

        int cmd;
        if (this.reservedItemIDs.containsKey(namespaceKey)) {
            cmd = this.reservedItemIDs.get(namespaceKey);
        } else {
            if (!this.currentItemIDs.containsKey(material)) {
                cmd = 1;
                this.currentItemIDs.put(material, 1);
            } else {
                cmd = this.currentItemIDs.get(material) + 1;
            }

            while (this.reservedItemIDs.containsValue(cmd)) { //TODO: Make a better and efficient way to do this, it must take materials into account
                cmd++;
            }
            if (cmd > 9999999) {
                throw new IllegalStateException("Cannot register more than 9999999 textures for a single item.");
            }

            this.currentItemIDs.replace(material, cmd);

            this.config.getConfig().set("reserved.items." + namespaceKey, cmd);
            this.config.saveConfig();
        }

        this.itemData.put(namespaceKey, new ItemData(cmd, material, handheld, blockPlacer, texture));

        return cmd;
    }

    @Override
    public int reserveItemCMD(@NonNull String namespaceKey, @NonNull Material material) {
        return this.registerItemTexture(namespaceKey, material, null);
    }

    @Override
    public TexturePackManager.BlockData registerBlockTexture(@NonNull String namespaceKey, @Nullable Path texture) {
        this.checkRegistration();

        if (this.blockData.containsKey(namespaceKey)) {
            throw new IllegalStateException("Another block has the same namespace key, cannot register the same block texture twice.");
        }

        int blockId;
        if (this.reservedBlockIDs.containsKey(namespaceKey)) {
            blockId = this.reservedBlockIDs.get(namespaceKey);
        } else {
            blockId = this.currentBlockID + 1;

            while (this.reservedBlockIDs.containsValue(blockId)) {
                blockId++;
            }
            if (blockId > OverridableBlockstates.availableStates.size()) {
                throw new IllegalStateException("Cannot register more block textures than available blockstates.");
            }

            this.currentBlockID = blockId;

            this.config.getConfig().set("reserved.blocks." + namespaceKey, blockId);
            this.config.saveConfig();
        }

        OverridableBlockstates.Blockstate blockstate = OverridableBlockstates.availableStates.get(blockId - 1);
        TexturePackManager.BlockData blockData = new TexturePackManager.BlockData(blockstate.up(), blockstate.down(), blockstate.north(), blockstate.east(), blockstate.south(), blockstate.west(), blockstate.parent());

        this.blockData.put(namespaceKey, new BlockData(blockData, texture));

        return blockData;
    }

    @Override
    public TexturePackManager.BlockData reserveBlockstate(@NonNull String namespaceKey) {
        return this.registerBlockTexture(namespaceKey, null);
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

        for (ItemData item : this.itemData.values()) {
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

        int index = 1;
        for (BlockData block : this.blockData.values()) {
            if (block.texture() == null) {
                continue;
            }

            TexturePackManager.BlockData data = block.blockData();
            String packPath = "block/custom/block_" + index++;

            Path blockstate = assetPath.resolve("blockstates").resolve(data.parent().name().toLowerCase(Locale.ROOT) + ".json");
            ObjectNode root = (ObjectNode) mapper.readTree(blockstate.toFile());

            //Find the correct variant with data. up, down, etc.
            //multipart is part of root, it is an array
            //the array contains the following structure: {"when": {"down": BOOL, "east": BOOL, "north": BOOL, "south": BOOL, "up": BOOL, "west": BOOL}, "apply": {"model": "PATH"}}
            //we need to find the correct variant, and then change the model path

            ArrayNode multipart = (ArrayNode) root.get("multipart");
            for (JsonNode node : multipart) {
                ObjectNode variant = (ObjectNode) node;
                ObjectNode when = (ObjectNode) variant.get("when");
                if (when.get("up").asBoolean() == data.up() &&
                        when.get("down").asBoolean() == data.down() &&
                        when.get("north").asBoolean() == data.north() &&
                        when.get("south").asBoolean() == data.south() &&
                        when.get("east").asBoolean() == data.east() &&
                        when.get("west").asBoolean() == data.west()) {
                    ObjectNode apply = (ObjectNode) variant.get("apply");
                    apply.remove("model");
                    apply.put("model", packPath);
                    break;
                }
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(blockstate.toFile(), root);

            Path customModel = modelsPath.resolve(packPath + ".json");
            if (Files.exists(customModel)) {
                Files.delete(customModel);
            }

            String content = "{\n" + //TODO: Should probably also support different textures per side
                    "  \"parent\": \"block/cube_all\",\n" +
                    "  \"textures\": {\n" +
                    "    \"all\": \"" + packPath + "\"\n" +
                    "  }\n" +
                    "}";
            Files.writeString(customModel, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);

            Path customTexture = texturesPath.resolve(packPath + ".png");
            if (Files.exists(customTexture)) {
                Files.delete(customTexture);
            }
            Files.copy(block.texture(), customTexture);
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

            Files.delete(zipPath);
        } catch (Exception e) {
            this.datapack.getLogger().severe("Failed to finalise the texture pack generation.");
            e.printStackTrace();
            this.enabled = false;
            return;
        }

        this.datapack.getLogger().info("Texture pack generation complete. It will be sent to players when they join the server.");

        this.reservedItemIDs.clear();
        this.reservedBlockIDs.clear();
        this.currentItemIDs.clear();
        this.currentBlockID = 0;
        this.itemData.clear();
        this.blockData.clear();
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
