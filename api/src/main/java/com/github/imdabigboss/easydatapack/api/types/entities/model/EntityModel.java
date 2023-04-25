package com.github.imdabigboss.easydatapack.api.types.entities.model;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;

/**
 * This class represents an entity model.
 */
public interface EntityModel {
    /**
     * Creates a new builder for an entity model.
     * @param root the root bone (or element) of the model
     * @return the new builder
     */
    static Builder builder(EntityBone root) {
        return (Builder) EasyDatapackAPI.createBuilder(Builder.class, root);
    }

    /**
     * Gets the root bone (or element) of the model.
     * @return the root bone of the model
     */
    EntityBone getRoot();

    /**
     * This class represents a builder for an entity model.
     */
    interface Builder extends GenericBuilder<EntityModel> {
    }
}
