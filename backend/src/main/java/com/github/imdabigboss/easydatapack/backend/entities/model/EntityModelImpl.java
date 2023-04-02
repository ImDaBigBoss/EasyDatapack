package com.github.imdabigboss.easydatapack.backend.entities.model;

import com.github.imdabigboss.easydatapack.api.entities.model.EntityBone;
import com.github.imdabigboss.easydatapack.api.entities.model.EntityModel;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EntityModelImpl implements EntityModel, Cloneable {
    private EntityBoneImpl root;

    private EntityModelImpl(EntityBoneImpl root) {
        this.root = root;
    }

    @Override
    public EntityBoneImpl getRoot() {
        return this.root;
    }

    public void spawn(Entity parent, Location location) {
        ItemDisplay item = this.root.spawn(location);
        this.root.propagateTransform(null);
        parent.addPassenger(item);
    }

    public void despawn() {
        this.root.despawn();
    }

    @Override
    public EntityModelImpl clone() {
        try {
            EntityModelImpl clone = (EntityModelImpl) super.clone();

            clone.root = this.root.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class BuilderImpl implements Builder, GenericBuilderImpl {
        private final EntityBoneImpl root;

        public BuilderImpl(EntityBone root) {
            this.root = (EntityBoneImpl) root;
        }

        @Override
        public @NonNull EntityModel build() {
            return new EntityModelImpl(this.root);
        }
    }
}
