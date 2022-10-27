package com.github.imdabigboss.easydatapack.api.items;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomHatItem extends CustomItem {
    protected CustomHatItem(int customModelData, String namespaceKey, String name, Material baseMaterial, boolean unbreakable, boolean hideFlags, Class<? extends Listener> eventListener, Consumer<PlayerInteractEvent> itemUseEvent, boolean spacingBeforeLore, String[] lore, Map<Attribute, List<AttributeModifier>> attributeModifiers, Map<Enchantment, Integer> enchantments) {
        super(customModelData, namespaceKey, name, baseMaterial, unbreakable, hideFlags, false, eventListener, itemUseEvent, spacingBeforeLore, lore, attributeModifiers, enchantments);
    }

    public static class Builder extends CustomItem.Builder {
        public Builder(int customModelData, String namespaceKey, String name, Material baseMaterial) {
            super(customModelData, namespaceKey, name, baseMaterial);
        }

        @Override
        public CustomHatItem build() {
            return new CustomHatItem(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.unbreakable, this.hideFlags, this.eventListener, this.itemUseEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments);
        }
    }
}
