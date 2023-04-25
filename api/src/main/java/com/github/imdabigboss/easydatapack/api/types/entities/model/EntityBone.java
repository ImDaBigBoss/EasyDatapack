package com.github.imdabigboss.easydatapack.api.types.entities.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
import com.github.imdabigboss.easydatapack.api.utils.math.EulerAnglef;
import com.github.imdabigboss.easydatapack.api.utils.math.Vector3f;
import org.bukkit.Material;

/**
 * This class represents a bone (or element) of an entity model.
 */
public interface EntityBone {
    /**
     * Creates a new builder for an entity bone.
     * @param displayItemCustomModelData the custom model data of the display item
     * @param displayItemMaterial the material of the display item
     * @return the new builder
     */
    static Builder builder(int displayItemCustomModelData, Material displayItemMaterial) {
        return (Builder) EasyDatapackAPI.createBuilder(Builder.class, displayItemCustomModelData, displayItemMaterial);
    }

    /**
     * Gets the children of this bone.
     * @return the children of this bone
     */
    EntityBone[] getChildren();

    /**
     * Gets the custom model data of the display item.
     * @return the custom model data of the display item
     */
    int getDisplayItemCustomModelData();

    /**
     * Gets the material of the display item.
     * @return the material of the display item
     */
    Material getDisplayItemMaterial();

    /**
     * Gets the rotation of the bone.
     * @return the rotation of the bone
     */
    EulerAnglef getRotation();

    /**
     * Gets the position offset of the bone.
     * @return the position offset of the bone
     */
    Vector3f getOffset();

    /**
     * This class represents a builder for an entity bone.
     */
    interface Builder extends GenericBuilder<EntityBone> {
        /**
         * Adds a child to this bone.
         * @param child the child to add
         * @return the builder
         */
        Builder addChild(EntityBone child);

        /**
         * Sets the rotation of the bone.
         * @param angle the rotation
         * @return the builder
         */
        Builder rotation(EulerAnglef angle);

        default Builder rotation(float roll, float pitch, float yaw) {
            return rotation(new EulerAnglef(roll, pitch, yaw));
        }

        /**
         * Sets the position offset of the bone.
         * @param offset the position offset
         * @return the builder
         */
        Builder offset(Vector3f offset);

        default Builder offset(float x, float y, float z) {
            return offset(new Vector3f(x, y, z));
        }

        default Builder offset(JsonNode offset) {
            return offset((float) offset.get(0).asDouble(), (float) offset.get(1).asDouble(), (float) offset.get(2).asDouble());
        }
    }
}