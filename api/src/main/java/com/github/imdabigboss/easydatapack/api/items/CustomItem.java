package com.github.imdabigboss.easydatapack.api.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomItemException;
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

/**
 * This class represents a custom item.
 */
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
    private final AttributeInformation[] attributeModifiers;
    private final EnchantmentInformation[] enchantments;
    private final Enchantment[] allowedEnchantments;
    private final Enchantment[] forbiddenEnchantments;

    protected ItemStack itemStack;

    protected CustomItem(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, boolean unbreakable, boolean hideFlags, boolean newItem, @Nullable Class<? extends Listener> eventListener, @Nullable Consumer<PlayerInteractEvent> itemUseEvent, boolean spacingBeforeLore, @Nullable String[] lore, @NonNull Map<Attribute, List<AttributeModifier>> attributeModifiers, @NonNull Map<Enchantment, Integer> enchantments, @NonNull List<Enchantment> allowedEnchantments, @NonNull List<Enchantment> forbiddenEnchantments) {
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

        this.allowedEnchantments = allowedEnchantments.toArray(new Enchantment[0]);
        this.forbiddenEnchantments = forbiddenEnchantments.toArray(new Enchantment[0]);

        this.itemStack.setItemMeta(itemMeta);
    }

    /**
     * Gets the custom model data of the item.
     * @return the custom model data of the item
     */
    public int getCustomModelData() {
        return this.customModelData;
    }

    /**
     * Gets the namespace key of the item.
     * @return the namespace key of the item
     */
    public @NonNull String getNamespaceKey() {
        return this.namespaceKey;
    }

    /**
     * Gets the namespaced key of the item.
     * @return the namespaced key of the item
     */
    public @NonNull NamespacedKey getNamespacedKey() {
        return new NamespacedKey(EasyDatapackAPI.NAMESPACE_KEY, this.namespaceKey);
    }

    /**
     * Gets the name of the item.
     * @return the name of the item
     */
    public @NonNull String getName() {
        return this.name;
    }

    /**
     * Gets the base material of the item.
     * @return the base material of the item
     */
    public @NonNull Material getBaseMaterial() {
        return this.baseMaterial;
    }

    /**
     * Gets if the item is unbreakable.
     * @return if the item is unbreakable
     */
    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    /**
     * Gets if the item hides flags.
     * @return if the item hides flags
     */
    public boolean isHideFlags() {
        return this.hideFlags;
    }

    /**
     * Gets if the item is a new item or if it is an extension of an existing item.
     * @return if the item is a new item
     */
    public boolean isNewItem() {
        return this.newItem;
    }

    /**
     * Gets the event listener of the item.
     * @return the event listener of the item
     */
    public @Nullable Class<? extends Listener> getEventListener() {
        return this.eventListener;
    }

    /**
     * Gets the event that is fired when the item is used.
     * @return the event that is fired when the item is used
     */
    public @Nullable Consumer<PlayerInteractEvent> getItemUseEvent() {
        return this.itemUseEvent;
    }

    /**
     * Gets the item's lore.
     * @return the item's lore
     */
    public @Nullable String[] getLore() {
        return this.lore;
    }

    /**
     * Gets the item's attribute modifiers.
     * @return the item's attribute modifiers
     */
    public @NonNull AttributeInformation[] getAttributeModifiers() {
        return this.attributeModifiers;
    }

    /**
     * Gets the item's enchantments.
     * @return the item's enchantments
     */
    public @NonNull EnchantmentInformation[] getEnchantments() {
        return this.enchantments;
    }

    /**
     * Gets a list of enchantments that can be applied to the item, this can be used if the base material doesn't allow
     * the enchantment by default.
     * @return a list of enchantments that can be applied to the item
     */
    public @Nullable Enchantment[] getAllowedEnchantments() {
        return this.allowedEnchantments;
    }

    /**
     * Gets a list of enchantments that can't be applied to the item, this can be used if the base material allows
     * the enchantment by default.
     * @return a list of enchantments that can't be applied to the item
     */
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

    /**
     * Gets the item stack of the item.
     * @return the item stack of the item
     */
    public @NonNull ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * This class represents the information about an attribute modifier.
     */
    public static class AttributeInformation {
        private final Attribute attribute;
        private final AttributeModifier[] modifier;

        /**
         * Creates a new attribute information.
         * @param attribute the attribute
         * @param modifier the modifier
         */
        public AttributeInformation(@NonNull Attribute attribute, @NonNull AttributeModifier[] modifier) {
            this.attribute = attribute;
            this.modifier = modifier;
        }

        /**
         * Gets the attribute.
         * @return the attribute
         */
        public @NonNull Attribute getAttribute() {
            return this.attribute;
        }

        /**
         * Gets the modifier.
         * @return the modifier
         */
        public @NonNull AttributeModifier[] getModifiers() {
            return this.modifier;
        }
    }

    /**
     * This class represents the information about an enchantment.
     */
    public static class EnchantmentInformation {
        private final Enchantment enchantment;
        private final int level;

        public EnchantmentInformation(@NonNull Enchantment enchantment, int level) {
            this.enchantment = enchantment;
            this.level = level;
        }

        public @NonNull Enchantment getEnchantment() {
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
        protected List<Enchantment> allowedEnchantments = new ArrayList<>();
        protected List<Enchantment> forbiddenEnchantments = new ArrayList<>();

        /**
         * Creates a new item builder.
         * @param customModelData the custom model data of the item
         * @param namespaceKey the namespace key of the item
         * @param name the name of the item
         * @param baseMaterial the base material of the item
         */
        public Builder(int customModelData, String namespaceKey, String name, Material baseMaterial) {
            this.customModelData = customModelData;
            this.namespaceKey = namespaceKey;
            this.name = name;
            this.baseMaterial = baseMaterial;
        }

        /**
         * Sets if the item is unbreakable.
         * @param unbreakable if the item is unbreakable
         * @return the builder
         */
        public @NonNull Builder unbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        /**
         * Sets if the item hides flags.
         * @param hideFlags if the item hides flags
         * @return the builder
         */
        public @NonNull Builder hideFlags(boolean hideFlags) {
            this.hideFlags = hideFlags;
            return this;
        }

        /**
         * Sets if the item is a new item or if it is an extension of an existing item.
         * @param newItem if the item is a new item
         * @return the builder
         */
        public @NonNull Builder newItem(boolean newItem) {
            this.newItem = newItem;
            return this;
        }

        /**
         * Sets the event listener of the item.
         * @param itemUseEvent the event listener of the item
         * @return the builder
         */
        public @NonNull Builder itemUseEvent(@Nullable Consumer<PlayerInteractEvent> itemUseEvent) {
            this.itemUseEvent = itemUseEvent;
            return this;
        }

        /**
         * Sets the event listener of the item.
         * @param eventListener the event listener of the item
         * @return the builder
         */
        public @NonNull Builder eventListener(@Nullable Class<? extends Listener> eventListener) {
            this.eventListener = eventListener;
            return this;
        }

        /**
         * Sets the item lore.
         * @param spacingBeforeLore if there should be spacing before the lore
         * @param lore the lore
         * @return the builder
         */
        public @NonNull Builder lore(boolean spacingBeforeLore, String... lore) {
            this.spacingBeforeLore = spacingBeforeLore;
            this.lore = lore;
            return this;
        }

        /**
         * Sets the item lore.
         * @param lore the lore
         * @return the builder
         */
        public @NonNull Builder lore(@Nullable String... lore) {
            this.spacingBeforeLore = true;
            this.lore = lore;
            return this;
        }

        /**
         * Adds an attribute modifier to the item.
         * @param attribute the attribute
         * @param attributeModifier the attribute modifier
         * @return the builder
         */
        public @NonNull Builder attributeModifier(@NonNull Attribute attribute, @NonNull AttributeModifier attributeModifier) {
            this.attributeModifiers.computeIfAbsent(attribute, k -> new ArrayList<>()).add(attributeModifier);
            return this;
        }

        /**
         * Adds an enchantment to the item.
         * @param enchantment the enchantment
         * @param level the level
         * @return the builder
         */
        public @NonNull Builder enchantment(@NonNull Enchantment enchantment, int level) {
            if (this.enchantments.containsKey(enchantment)) {
                throw new IllegalArgumentException("Enchantment " + enchantment.getKey() + " is already added to this item");
            }

            this.enchantments.put(enchantment, level);
            return this;
        }

        /**
         * Adds an allowed enchantment to the item, this can be used if the base material doesn't allow the enchantment
         * by default.
         * @param enchantments the enchantments
         * @return the builder
         */
        public @NonNull Builder allowedEnchantment(@NonNull Enchantment... enchantments) throws CustomItemException {
            for (Enchantment enchantment : enchantments) {
                if (this.forbiddenEnchantments.contains(enchantment)) {
                    throw new CustomItemException(enchantment.getKey().asString() + " can't be both allowed and forbidden");
                }

                this.allowedEnchantments.add(enchantment);
            }
            return this;
        }

        /**
         * Adds a forbidden enchantment to the item, this can be used if the base material allows the enchantment
         * by default.
         * @param enchantments the enchantments
         * @return the builder
         */
        public @NonNull Builder forbiddenEnchantment(@NonNull Enchantment... enchantments) throws CustomItemException {
            for (Enchantment enchantment : enchantments) {
                if (this.allowedEnchantments.contains(enchantment)) {
                    throw new CustomItemException(enchantment.getKey().asString() + " can't be both allowed and forbidden");
                }

                this.forbiddenEnchantments.add(enchantment);
            }
            return this;
        }

        /**
         * Builds the item.
         * @return the item
         */
        public @NonNull CustomItem build() {
            return new CustomItem(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.unbreakable, this.hideFlags, this.newItem, this.eventListener, this.itemUseEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments, this.allowedEnchantments, this.forbiddenEnchantments);
        }
    }
}
