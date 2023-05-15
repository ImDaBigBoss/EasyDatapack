package com.github.imdabigboss.easydatapack.api.types.entities.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imdabigboss.easydatapack.api.exceptions.EasyDatapackException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BedrockConverter {
    public static EntityModel fromBedrock(File file) throws EasyDatapackException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(file);
            return fromBedrock(root);
        } catch (Exception e) {
            throw new EasyDatapackException("Failed to read bedrock model file", e);
        }
    }

    public static EntityModel fromBedrock(JsonNode root) throws EasyDatapackException {
        JsonNode formatVersion = root.get("format_version");
        if (formatVersion != null && formatVersion.isTextual()) {
            if (!formatVersion.asText().equals("1.12.0")) {
                throw new EasyDatapackException("Unsupported format version: " + formatVersion.asText());
            }
        }

        JsonNode geometry = root.get("minecraft:geometry");
        if (geometry == null || !geometry.isArray()) {
            throw new EasyDatapackException("Missing or invalid minecraft:geometry array");
        }

        if (geometry.size() != 1) {
            throw new EasyDatapackException("Only one geometry per file is supported");
        }

        geometry = geometry.get(0);
        if (!geometry.isObject()) {
            throw new EasyDatapackException("Invalid geometry object");
        }

        JsonNode bones = geometry.get("bones");
        if (bones == null || !bones.isArray()) {
            throw new EasyDatapackException("Missing or invalid bones array");
        }

        Map<String, EntityBone.Builder> boneMap = new HashMap<>();
        Map<String, List<String>> parentMap = new HashMap<>();
        String rootBoneName = null;

        for (JsonNode bone : bones) {
            JsonNode name = bone.get("name");
            if (name == null || !name.isTextual()) {
                throw new EasyDatapackException("Missing or invalid bone name");
            }

            JsonNode parent = bone.get("parent");
            if (parent != null && !parent.isTextual()) {
                throw new EasyDatapackException("Invalid bone parent");
            }

            JsonNode pivot = bone.get("pivot");
            if (pivot != null && (!pivot.isArray() || pivot.size() != 3)) {
                throw new EasyDatapackException("Invalid bone pivot");
            }

            JsonNode rotation = bone.get("rotation");
            if (rotation != null && (!rotation.isArray() || rotation.size() != 3)) {
                throw new EasyDatapackException("Invalid bone rotation");
            }

            JsonNode cubes = bone.get("cubes");
            if (cubes == null || !cubes.isArray() || cubes.size() == 0) {
                throw new EasyDatapackException("Missing or invalid bone cubes");
            }

            //TODO: handle cubes
        }

        EntityBone rootBone = buildBoneTree(boneMap, parentMap, rootBoneName);

        return EntityModel.builder(rootBone).build();
    }

    private static EntityBone buildBoneTree(Map<String, EntityBone.Builder> boneMap, Map<String, List<String>> parentMap, String rootBoneName) {
        EntityBone.Builder rootBone = boneMap.get(rootBoneName);
        List<String> children = parentMap.get(rootBoneName);
        if (children != null) {
            for (String child : children) {
                rootBone.addChild(buildBoneTree(boneMap, parentMap, child));
            }
        }

        return rootBone.build();
    }
}
