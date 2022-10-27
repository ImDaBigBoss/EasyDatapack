package com.github.imdabigboss.easydatapack.api.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomItem {
    private final int customModelData;
    private final String namespaceKey;
    private final String name;
    private final Material baseMaterial;
    private final boolean unbreakable;
    private final boolean hideFlags;
    private final boolean newItem;
    private final Class<? extends Listener> eventListener;
    private final Consumer<PlayerInteractEvent> itemUseEvent;
    private final boolean spacingBeforeLore;
    private final String[] lore;
    private final AttributeInformation[] attributeModifiers; //TODO: use arrays
    private final EnchantmentInformation[] enchantments;

    protected ItemStack itemStack;

    protected CustomItem(int customModelData, String namespaceKey, String name, Material baseMaterial, boolean unbreakable, boolean hideFlags, boolean newItem, Class<? extends Listener> eventListener, Consumer<PlayerInteractEvent> itemUseEvent, boolean spacingBeforeLore, String[] lore, Map<Attribute, List<AttributeModifier>> attributeModifiers, Map<Enchantment, Integer> enchantments) {
        this.customModelData = customModelData;
        this.namespaceKey = namespaceKey;
        this.name = name;
        this.baseMaterial = baseMaterial;
        this.unbreakable = unbreakable;
        this.hideFlags = hideFlags;
        this.newItem = newItem;
        this.eventListener = eventListener;
        this.itemUseEvent = itemUseEvent;
        this.spacingBeforeLore = spacingBeforeLore;
        this.lore = lore;

        this.attributeModifiers = new AttributeInformation[attributeModifiers.size()];
        int i = 0;
        for (Map.Entry<Attribute, List<AttributeModifier>> entry : attributeModifiers.entrySet()) {
            this.attributeModifiers[i] = new AttributeInformation(entry.getKey(), entry.getValue().toArray(new AttributeModifier[0]));
            i++;
        }

        this.enchantments = new EnchantmentInformation[enchantments.size()];
        i = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            this.enchantments[i] = new EnchantmentInformation(entry.getKey(), entry.getValue());
            i++;
        }

        this.itemStack = new ItemStack(baseMaterial, 1);

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemMeta.setUnbreakable(unbreakable);
        itemMeta.displayName(Component.text(name));

        if (hideFlags) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        if (lore != null) {
            List<Component> loreList = new ArrayList<>();
            if (spacingBeforeLore) {
                loreList.add(Component.text(""));
            }
            for (String line : lore) {
                loreList.add(Component.text(ChatColor.WHITE + line));
            }
            itemMeta.lore(loreList);
        }

        for (AttributeInformation entry : this.attributeModifiers) {
            for (AttributeModifier modifier : entry.getModifiers()) {
                itemMeta.addAttributeModifier(entry.getAttribute(), modifier);
            }
        }

        for (EnchantmentInformation entry : this.enchantments) {
            itemMeta.addEnchant(entry.getEnchantment(), entry.getLevel(), true);
        }

        this.itemStack.setItemMeta(itemMeta);
    }

    public int getCustomModelData() {
        return this.customModelData;
    }

    public String getNamespaceKey() {
        return this.namespaceKey;
    }

    public NamespacedKey getNamespacedKey() {
        return new NamespacedKey(EasyDatapackAPI.NAMESPACE_KEY, this.namespaceKey);
    }

    public String getName() {
        return this.name;
    }

    public Material getBaseMaterial() {
        return this.baseMaterial;
    }

    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    public boolean isHideFlags() {
        return this.hideFlags;
    }

    public boolean isNewItem() {
        return this.newItem;
    }

    public Class<? extends Listener> getEventListener() {
        return this.eventListener;
    }

    public Consumer<PlayerInteractEvent> getItemUseEvent() {
        return this.itemUseEvent;
    }

    public String[] getLore() {
        return this.lore;
    }

    public AttributeInformation[] getAttributeModifiers() {
        return this.attributeModifiers;
    }

    public EnchantmentInformation[] getEnchantments() {
        return this.enchantments;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public static class AttributeInformation {
        private final Attribute attribute;
        private final AttributeModifier[] modifier;

        public AttributeInformation(Attribute attribute, AttributeModifier[] modifier) {
            this.attribute = attribute;
            this.modifier = modifier;
        }

        public Attribute getAttribute() {
            return this.attribute;
        }

        public AttributeModifier[] getModifiers() {
            return this.modifier;
        }
    }

    public static class EnchantmentInformation {
        private final Enchantment enchantment;
        private final int level;

        public EnchantmentInformation(Enchantment enchantment, int level) {
            this.enchantment = enchantment;
            this.level = level;
        }

        public Enchantment getEnchantment() {
            return this.enchantment;
        }

        public int getLevel() {
            return this.level;
        }
    }

    public static class Builder {
        protected final int customModelData;
        protected final String namespaceKey;
        protected final String name;
        protected final Material baseMaterial;

        protected boolean unbreakable = false;
        protected boolean hideFlags = false;
        protected boolean newItem = true;
        protected Consumer<PlayerInteractEvent> itemUseEvent = null;
        protected Class<? extends Listener> eventListener = null;
        protected boolean spacingBeforeLore = true;
        protected String[] lore = null;
        protected Map<Attribute, List<AttributeModifier>> attributeModifiers = new HashMap<>();
        protected Map<Enchantment, Integer> enchantments = new HashMap<>();

        public Builder(int customModelData, String namespaceKey, String name, Material baseMaterial) {
            this.customModelData = customModelData;
            this.namespaceKey = namespaceKey;
            this.name = name;
            this.baseMaterial = baseMaterial;
        }

        public Builder unbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        public Builder hideFlags(boolean hideFlags) {
            this.hideFlags = hideFlags;
            return this;
        }

        public Builder newItem(boolean newItem) {
            this.newItem = newItem;
            return this;
        }

        public Builder itemUseEvent(Consumer<PlayerInteractEvent> itemUseEvent) {
            this.itemUseEvent = itemUseEvent;
            return this;
        }

        public Builder eventListener(Class<? extends Listener> eventListener) {
            this.eventListener = eventListener;
            return this;
        }

        public Builder lore(boolean spacingBeforeLore, String... lore) {
            this.spacingBeforeLore = spacingBeforeLore;
            this.lore = lore;
            return this;
        }

        public Builder lore(String... lore) {
            this.spacingBeforeLore = true;
            this.lore = lore;
            return this;
        }

        public Builder attributeModifier(Attribute attribute, AttributeModifier attributeModifier) {
            this.attributeModifiers.computeIfAbsent(attribute, k -> new ArrayList<>()).add(attributeModifier);
            return this;
        }

        public Builder enchantment(Enchantment enchantment, int level) {
            if (this.enchantments.containsKey(enchantment)) {
                throw new IllegalArgumentException("Enchantment " + enchantment.getKey() + " is already added to this item");
            }

            this.enchantments.put(enchantment, level);
            return this;
        }

        public CustomItem build() {
            return new CustomItem(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.unbreakable, this.hideFlags, this.newItem, this.eventListener, this.itemUseEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments);
        }
    }
}
