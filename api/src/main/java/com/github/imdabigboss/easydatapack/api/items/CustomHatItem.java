package com.github.imdabigboss.easydatapack.api.items;

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

/**
 * This class represents a custom item that is a hat, and thus can be worn.
 */
public class CustomHatItem extends CustomItem {
    private CustomHatItem(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, boolean unbreakable, boolean hideFlags, @Nullable Class<? extends Listener> eventListener, @Nullable Consumer<PlayerInteractEvent> itemUseEvent, boolean spacingBeforeLore, @Nullable String[] lore, @NonNull Map<Attribute, List<AttributeModifier>> attributeModifiers, @NonNull Map<Enchantment, Integer> enchantments, @NonNull List<Enchantment> allowedEnchantments, @NonNull List<Enchantment> forbiddenEnchantments) {
        super(customModelData, namespaceKey, name, baseMaterial, unbreakable, hideFlags, false, eventListener, itemUseEvent, spacingBeforeLore, lore, attributeModifiers, enchantments, allowedEnchantments, forbiddenEnchantments);
    }

    /**
     * This class represents a builder for a custom hat item.
     */
    public static class Builder extends CustomItem.Builder {
        /**
         * Creates a new builder for a custom hat item.
         * @param customModelData the custom model data of the item
         * @param namespaceKey the namespace key of the item
         * @param name the name of the item
         * @param baseMaterial the base material of the item
         */
        public Builder(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial) {
            super(customModelData, namespaceKey, name, baseMaterial);
        }

        /**
         * Builds the custom hat item.
         * @return the custom hat item
         */
        @Override
        public @NonNull CustomHatItem build() {
            return new CustomHatItem(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.unbreakable, this.hideFlags, this.eventListener, this.itemUseEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments, this.allowedEnchantments, this.forbiddenEnchantments);
        }
    }
}
