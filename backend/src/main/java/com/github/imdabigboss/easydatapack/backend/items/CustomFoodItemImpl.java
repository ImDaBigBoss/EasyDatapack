package com.github.imdabigboss.easydatapack.backend.items;

import com.github.imdabigboss.easydatapack.api.items.CustomFoodItem;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomFoodItemImpl extends CustomItemImpl implements CustomFoodItem {
    private final int nutrition;
    private final float saturation;
    private final Material residue;

    private CustomFoodItemImpl(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, int nutrition, float saturation, @Nullable Material residue, boolean unbreakable, boolean hideFlags, boolean newItem, @Nullable Class<? extends Listener> eventListener, @Nullable Consumer<PlayerInteractEvent> itemUseEvent, boolean spacingBeforeLore, @Nullable String[] lore, @NonNull Map<Attribute, List<AttributeModifier>> attributeModifiers, @NonNull Map<Enchantment, Integer> enchantments, @NonNull List<Enchantment> allowedEnchantments, @NonNull List<Enchantment> forbiddenEnchantments) {
        super(customModelData, namespaceKey, name, baseMaterial, unbreakable, hideFlags, newItem, eventListener, itemUseEvent, spacingBeforeLore, lore, attributeModifiers, enchantments, allowedEnchantments, forbiddenEnchantments);

        this.nutrition = nutrition;
        this.saturation = saturation;
        this.residue = residue;
    }

    @Override
    public int getNutrition() {
        return this.nutrition;
    }

    @Override
    public float getSaturation() {
        return this.saturation;
    }

    @Override
    public Material getResidue() {
        return this.residue;
    }

    public static class FoodBudilerImpl extends BuilderImpl implements FoodBuilder {
        private final int nutrition;
        private final float saturation;

        private Material residue = null;

        public FoodBudilerImpl(int customModelData, String namespaceKey, String name, Material baseMaterial, int nutrition, float saturation) {
            super(customModelData, namespaceKey, name, baseMaterial);

            this.nutrition = nutrition;
            this.saturation = saturation;
        }

        @Override
        public FoodBuilder residue(@Nullable Material residue) {
            this.residue = residue;
            return this;
        }

        @Override
        public @NonNull CustomFoodItem build() {
            return new CustomFoodItemImpl(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.nutrition, this.saturation, this.residue, this.unbreakable, this.hideFlags, this.newItem, this.eventListener, this.itemUseEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments, this.allowedEnchantments, this.forbiddenEnchantments);
        }
    }
}
