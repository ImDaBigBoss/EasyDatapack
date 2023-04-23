package com.github.imdabigboss.easydatapack.backend.types.entities.model;

import com.github.imdabigboss.easydatapack.api.entities.model.EntityBone;
import com.github.imdabigboss.easydatapack.api.utils.math.EulerAnglef;
import com.github.imdabigboss.easydatapack.api.utils.math.Vector3f;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public class EntityBoneImpl implements EntityBone, Cloneable {
    private final int displayItemCustomModelData;
    private final Material displayItemMaterial;
    private EntityBoneImpl[] children;

    private EulerAnglef rotation;
    private Vector3f offset;

    private EulerAnglef currentRotation = null;
    private Vector3f currentOffset = null;

    private ItemDisplay display = null;

    private EntityBoneImpl(int displayItemCustomModelData, Material displayItemMaterial, EntityBoneImpl[] children, EulerAnglef rotation, Vector3f offset) {
        this.displayItemCustomModelData = displayItemCustomModelData;
        this.displayItemMaterial = displayItemMaterial;
        this.children = children;

        this.rotation = rotation;
        this.offset = offset;
    }

    @Override
    public EntityBone[] getChildren() {
        return this.children;
    }

    @Override
    public int getDisplayItemCustomModelData() {
        return this.displayItemCustomModelData;
    }

    @Override
    public Material getDisplayItemMaterial() {
        return this.displayItemMaterial;
    }

    @Override
    public EulerAnglef getRotation() {
        return this.rotation;
    }

    @Override
    public Vector3f getOffset() {
        return this.offset;
    }

    private void init() {
        this.currentRotation = this.rotation.clone();
        this.currentOffset = this.offset.clone();
    }

    public void propagateTransform(Transformation parent) {
        Quaternionf leftRotation;
        if (parent == null) {
            leftRotation = new Quaternionf();
        } else {
            leftRotation = parent.getRightRotation();
        }

        Quaternionf rightRotation = this.currentRotation.toQuaternion();

        org.joml.Vector3f offset = this.currentOffset.toBlockJOML();
        if (parent != null) {
            offset.add(parent.getTranslation());
        }

        Transformation transformation = new Transformation(offset, leftRotation, new org.joml.Vector3f(1), rightRotation);
        this.display.setTransformation(transformation);

        for (EntityBoneImpl child : this.children) {
            child.propagateTransform(transformation);
        }
    }

    public ItemDisplay spawn(Location location) {
        if (this.display != null) {
            throw new IllegalStateException("Display already created");
        }

        this.init();

        ItemStack displayItem = new ItemStack(this.displayItemMaterial);
        ItemMeta meta = displayItem.getItemMeta();
        meta.setCustomModelData(this.displayItemCustomModelData);
        displayItem.setItemMeta(meta);

        this.display = location.getWorld().spawn(location, ItemDisplay.class);
        this.display.setPersistent(true);

        this.display.setItemStack(displayItem);
        this.display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);

        for (EntityBoneImpl child : this.children) {
            child.spawn(location);
            this.display.addPassenger(child.getDisplay());
        }

        return this.display;
    }

    public void despawn() {
        if (this.display == null) {
            throw new IllegalStateException("Display not created");
        }

        this.display.remove();

        for (EntityBoneImpl child : this.children) {
            child.despawn();
        }
    }

    public ItemDisplay getDisplay() {
        return this.display;
    }

    @Override
    public EntityBoneImpl clone() {
        try {
            EntityBoneImpl clone = (EntityBoneImpl) super.clone();

            clone.children = new EntityBoneImpl[this.children.length];
            for (int i = 0; i < this.children.length; i++) {
                clone.children[i] = this.children[i].clone();
            }

            clone.rotation = this.rotation.clone();
            clone.offset = this.offset.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class BuilderImpl implements Builder, GenericBuilderImpl {
        private final int displayItemCustomModelData;
        private final Material displayItemMaterial;

        private final List<EntityBoneImpl> children = new ArrayList<>();
        private EulerAnglef rotation = new EulerAnglef();
        private Vector3f offset = new Vector3f();

        public BuilderImpl(int displayItemCustomModelData, Material displayItemMaterial) {
            this.displayItemCustomModelData = displayItemCustomModelData;
            this.displayItemMaterial = displayItemMaterial;
        }

        @Override
        public Builder addChild(EntityBone child) {
            this.children.add((EntityBoneImpl) child);
            return this;
        }

        @Override
        public Builder rotation(EulerAnglef angle) {
            this.rotation = angle;
            return this;
        }

        @Override
        public Builder offset(Vector3f angle) {
            this.offset = angle;
            return this;
        }

        @Override
        public @NonNull EntityBone build() {
            return new EntityBoneImpl(this.displayItemCustomModelData, this.displayItemMaterial, this.children.toArray(new EntityBoneImpl[0]), this.rotation, this.offset);
        }
    }
}
