package com.github.imdabigboss.easydatapack.backend.types.items;

import com.github.imdabigboss.easydatapack.api.types.blocks.CustomBlock;
import com.github.imdabigboss.easydatapack.api.types.items.CustomBlockPlacerItem;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomBlockPlacerItemImpl extends CustomItemImpl implements CustomBlockPlacerItem {
    private final Consumer<BlockPlaceEvent> blockPlaceEvent;
    private CustomBlock block = null;

    private CustomBlockPlacerItemImpl(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, @Nullable Consumer<BlockPlaceEvent> blockPlaceEvent, boolean unbreakable, boolean hideFlags, boolean newItem, @Nullable Class<? extends Listener> eventListener, @Nullable Consumer<PlayerInteractEvent> itemUseEvent, boolean spacingBeforeLore, @Nullable String[] lore, @NonNull Map<Attribute, List<AttributeModifier>> attributeModifiers, @NonNull Map<Enchantment, Integer> enchantments, @NonNull List<Enchantment> allowedEnchantments, @NonNull List<Enchantment> forbiddenEnchantments) {
        super(customModelData, namespaceKey, name, baseMaterial, unbreakable, hideFlags, newItem, eventListener, itemUseEvent, spacingBeforeLore, lore, attributeModifiers, enchantments, allowedEnchantments, forbiddenEnchantments);

        this.blockPlaceEvent = blockPlaceEvent;
    }

    @Override
    public @Nullable Consumer<BlockPlaceEvent> getBlockPlaceEvent() {
        return this.blockPlaceEvent;
    }

    public CustomBlock getPlacedBlock() {
        return this.block;
    }

    public void setPlacedBlock(CustomBlock block) {
        this.block = block;
    }

    public static class BlockPlacerBuilderImpl extends BuilderImpl implements BlockPlacerBuilder {
        private Consumer<BlockPlaceEvent> blockPlaceEvent = null;

        public BlockPlacerBuilderImpl(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial) {
            super(customModelData, namespaceKey, name, baseMaterial);
        }

        @Override
        public @NonNull BlockPlacerBuilder blockPlaceEvent(@Nullable Consumer<BlockPlaceEvent> blockPlaceEvent) {
            this.blockPlaceEvent = blockPlaceEvent;
            return this;
        }

        @Override
        public @NonNull CustomBlockPlacerItemImpl build() {
            return new CustomBlockPlacerItemImpl(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.blockPlaceEvent, this.unbreakable, this.hideFlags, this.newItem, this.eventListener, this.itemUseEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments, this.allowedEnchantments, this.forbiddenEnchantments);
        }
    }
}
