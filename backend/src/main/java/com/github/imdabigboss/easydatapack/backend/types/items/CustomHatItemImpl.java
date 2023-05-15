package com.github.imdabigboss.easydatapack.backend.types.items;

import com.github.imdabigboss.easydatapack.api.types.items.CustomHatItem;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomHatItemImpl extends CustomItemImpl implements CustomHatItem, GenericBuilderImpl {
    private CustomHatItemImpl(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, boolean unbreakable, boolean hideFlags, boolean newItem, @Nullable Class<? extends Listener> eventListener, @Nullable Consumer<PlayerInteractEvent> itemUseEvent, boolean spacingBeforeLore, @Nullable String[] lore, @NonNull Map<Attribute, List<AttributeModifier>> attributeModifiers, @NonNull Map<Enchantment, Integer> enchantments, @NonNull List<Enchantment> allowedEnchantments, @NonNull List<Enchantment> forbiddenEnchantments) {
        super(customModelData, namespaceKey, name, baseMaterial, unbreakable, hideFlags, newItem, eventListener, itemUseEvent, spacingBeforeLore, lore, attributeModifiers, enchantments, allowedEnchantments, forbiddenEnchantments);
    }

    public static class HatBuilderImpl extends BuilderImpl implements HatBuilder {
        public HatBuilderImpl(int customModelData, String namespaceKey, String name, Material baseMaterial) {
            super(customModelData, namespaceKey, name, baseMaterial);
        }

        @Override
        public @NonNull CustomHatItem build() {
            return new CustomHatItemImpl(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.unbreakable, this.hideFlags, this.newItem, this.eventListener, this.itemUseEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments, this.allowedEnchantments, this.forbiddenEnchantments);
        }
    }
}
