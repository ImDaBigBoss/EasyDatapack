package com.github.imdabigboss.easydatapack.backend.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomItemException;
import com.github.imdabigboss.easydatapack.api.items.CustomItem;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CustomItemImpl implements CustomItem {
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
    private final com.github.imdabigboss.easydatapack.api.items.CustomItem.AttributeInformation[] attributeModifiers;
    private final com.github.imdabigboss.easydatapack.api.items.CustomItem.EnchantmentInformation[] enchantments;
    private final Enchantment[] allowedEnchantments;
    private final Enchantment[] forbiddenEnchantments;

    protected ItemStack itemStack;

    protected CustomItemImpl(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, boolean unbreakable, boolean hideFlags, boolean newItem, @Nullable Class<? extends Listener> eventListener, @Nullable Consumer<PlayerInteractEvent> itemUseEvent, boolean spacingBeforeLore, @Nullable String[] lore, @NonNull Map<Attribute, List<AttributeModifier>> attributeModifiers, @NonNull Map<Enchantment, Integer> enchantments, @NonNull List<Enchantment> allowedEnchantments, @NonNull List<Enchantment> forbiddenEnchantments) {
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

        this.attributeModifiers = new com.github.imdabigboss.easydatapack.api.items.CustomItem.AttributeInformation[attributeModifiers.size()];
        int i = 0;
        for (Map.Entry<Attribute, List<AttributeModifier>> entry : attributeModifiers.entrySet()) {
            this.attributeModifiers[i] = new com.github.imdabigboss.easydatapack.api.items.CustomItem.AttributeInformation(entry.getKey(), entry.getValue().toArray(new AttributeModifier[0]));
            i++;
        }

        this.enchantments = new com.github.imdabigboss.easydatapack.api.items.CustomItem.EnchantmentInformation[enchantments.size()];
        i = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            this.enchantments[i] = new com.github.imdabigboss.easydatapack.api.items.CustomItem.EnchantmentInformation(entry.getKey(), entry.getValue());
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

        for (com.github.imdabigboss.easydatapack.api.items.CustomItem.AttributeInformation entry : this.attributeModifiers) {
            for (AttributeModifier modifier : entry.modifiers()) {
                itemMeta.addAttributeModifier(entry.attribute(), modifier);
            }
        }

        for (com.github.imdabigboss.easydatapack.api.items.CustomItem.EnchantmentInformation entry : this.enchantments) {
            itemMeta.addEnchant(entry.enchantment(), entry.level(), true);
        }

        this.allowedEnchantments = allowedEnchantments.toArray(new Enchantment[0]);
        this.forbiddenEnchantments = forbiddenEnchantments.toArray(new Enchantment[0]);

        this.itemStack.setItemMeta(itemMeta);
    }

    @Override
    public int getCustomModelData() {
        return this.customModelData;
    }

    @Override
    public @NonNull String getNamespaceKey() {
        return this.namespaceKey;
    }

    @Override
    public @NonNull NamespacedKey getNamespacedKey() {
        return new NamespacedKey(EasyDatapackAPI.NAMESPACE_KEY, this.namespaceKey);
    }

    @Override
    public @NonNull String getName() {
        return this.name;
    }

    @Override
    public @NonNull Material getBaseMaterial() {
        return this.baseMaterial;
    }

    @Override
    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    @Override
    public boolean isHideFlags() {
        return this.hideFlags;
    }

    @Override
    public boolean isNewItem() {
        return this.newItem;
    }

    @Override
    public @Nullable Class<? extends Listener> getEventListener() {
        return this.eventListener;
    }

    @Override
    public @Nullable Consumer<PlayerInteractEvent> getItemUseEvent() {
        return this.itemUseEvent;
    }

    @Override
    public @Nullable String[] getLore() {
        return this.lore;
    }

    @Override
    public @NonNull com.github.imdabigboss.easydatapack.api.items.CustomItem.AttributeInformation[] getAttributeModifiers() {
        return this.attributeModifiers;
    }

    @Override
    public @NonNull com.github.imdabigboss.easydatapack.api.items.CustomItem.EnchantmentInformation[] getEnchantments() {
        return this.enchantments;
    }

    @Override
    public @Nullable Enchantment[] getAllowedEnchantments() {
        return this.allowedEnchantments;
    }

    @Override
    public @Nullable Enchantment[] getForbiddenEnchantments() {
        return this.forbiddenEnchantments;
    }

    public @NonNull TriState canEnchant(@NonNull Enchantment enchantment) {
        for (Enchantment allowedEnchantment : this.allowedEnchantments) {
            if (allowedEnchantment.equals(enchantment)) {
                return TriState.TRUE;
            }
        }
        for (Enchantment forbiddenEnchantment : this.forbiddenEnchantments) {
            if (forbiddenEnchantment.equals(enchantment)) {
                return TriState.FALSE;
            }
        }
        return TriState.NOT_SET;
    }

    @Override
    public @NonNull ItemStack getItemStack() {
        return this.itemStack;
    }

    public static class BuilderImpl implements Builder, GenericBuilderImpl {
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
        protected final Map<Attribute, List<AttributeModifier>> attributeModifiers = new HashMap<>();
        protected final Map<Enchantment, Integer> enchantments = new HashMap<>();
        protected final List<Enchantment> allowedEnchantments = new ArrayList<>();
        protected final List<Enchantment> forbiddenEnchantments = new ArrayList<>();

        public BuilderImpl(int customModelData, String namespaceKey, String name, Material baseMaterial) {
            this.customModelData = customModelData;
            this.namespaceKey = namespaceKey;
            this.name = name;
            this.baseMaterial = baseMaterial;
        }

        @Override
        public @NonNull Builder unbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        @Override
        public @NonNull Builder hideFlags(boolean hideFlags) {
            this.hideFlags = hideFlags;
            return this;
        }

        @Override
        public @NonNull Builder newItem(boolean newItem) {
            this.newItem = newItem;
            return this;
        }

        @Override
        public @NonNull Builder itemUseEvent(@Nullable Consumer<PlayerInteractEvent> itemUseEvent) {
            this.itemUseEvent = itemUseEvent;
            return this;
        }

        @Override
        public @NonNull Builder eventListener(@Nullable Class<? extends Listener> eventListener) {
            this.eventListener = eventListener;
            return this;
        }

        @Override
        public @NonNull Builder lore(boolean spacingBeforeLore, String... lore) {
            this.spacingBeforeLore = spacingBeforeLore;
            this.lore = lore;
            return this;
        }

        @Override
        public @NonNull Builder lore(@Nullable String... lore) {
            this.spacingBeforeLore = true;
            this.lore = lore;
            return this;
        }

        @Override
        public @NonNull Builder attributeModifier(@NonNull Attribute attribute, @NonNull AttributeModifier attributeModifier) {
            this.attributeModifiers.computeIfAbsent(attribute, k -> new ArrayList<>()).add(attributeModifier);
            return this;
        }

        @Override
        public @NonNull Builder enchantment(@NonNull Enchantment enchantment, int level) {
            if (this.enchantments.containsKey(enchantment)) {
                throw new IllegalArgumentException("Enchantment " + enchantment.getKey() + " is already added to this item");
            }

            this.enchantments.put(enchantment, level);
            return this;
        }

        @Override
        public @NonNull Builder allowedEnchantment(@NonNull Enchantment... enchantments) throws CustomItemException {
            for (Enchantment enchantment : enchantments) {
                if (this.forbiddenEnchantments.contains(enchantment)) {
                    throw new CustomItemException(enchantment.getKey().asString() + " can't be both allowed and forbidden");
                }

                this.allowedEnchantments.add(enchantment);
            }
            return this;
        }

        @Override
        public @NonNull Builder forbiddenEnchantment(@NonNull Enchantment... enchantments) throws CustomItemException {
            for (Enchantment enchantment : enchantments) {
                if (this.allowedEnchantments.contains(enchantment)) {
                    throw new CustomItemException(enchantment.getKey().asString() + " can't be both allowed and forbidden");
                }

                this.forbiddenEnchantments.add(enchantment);
            }
            return this;
        }

        @Override
        public @NonNull CustomItem build() {
            return new CustomItemImpl(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.unbreakable, this.hideFlags, this.newItem, this.eventListener, this.itemUseEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments, this.allowedEnchantments, this.forbiddenEnchantments);
        }
    }
}
