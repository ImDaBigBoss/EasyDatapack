package com.github.imdabigboss.easydatapack.api.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.exceptions.CustomItemException;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
import net.kyori.adventure.util.TriState;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * This class represents a custom item.
 */
public interface CustomItem {
    /**
     * Creates a new item builder.
     * @param customModelData the custom model data of the item
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     */
    static CustomItem.Builder builder(int customModelData, String namespaceKey, String name, Material baseMaterial) {
        return (CustomItem.Builder) EasyDatapackAPI.createBuilder(Builder.class, customModelData, namespaceKey, name, baseMaterial);
    }

    /**
     * Gets the custom model data of the item.
     * @return the custom model data of the item
     */
    int getCustomModelData();

    /**
     * Gets the namespace key of the item.
     * @return the namespace key of the item
     */
    @NonNull String getNamespaceKey();

    /**
     * Gets the namespaced key of the item.
     * @return the namespaced key of the item
     */
    @NonNull NamespacedKey getNamespacedKey();

    /**
     * Gets the name of the item.
     * @return the name of the item
     */
    @NonNull String getName();

    /**
     * Gets the base material of the item.
     * @return the base material of the item
     */
    @NonNull Material getBaseMaterial();

    /**
     * Gets if the item is unbreakable.
     * @return if the item is unbreakable
     */
    boolean isUnbreakable();

    /**
     * Gets if the item hides flags.
     * @return if the item hides flags
     */
    boolean isHideFlags();

    /**
     * Gets if the item is a new item or if it is an extension of an existing item.
     * @return if the item is a new item
     */
    boolean isNewItem();

    /**
     * Gets the event listener of the item.
     * @return the event listener of the item
     */
    @Nullable Class<? extends Listener> getEventListener();

    /**
     * Gets the event that is fired when the item is used.
     * @return the event that is fired when the item is used
     */
    @Nullable Consumer<PlayerInteractEvent> getItemUseEvent();

    /**
     * Gets the item's lore.
     * @return the item's lore
     */
    @Nullable String[] getLore();

    /**
     * Gets the item's attribute modifiers.
     * @return the item's attribute modifiers
     */
    @NonNull AttributeInformation[] getAttributeModifiers();

    /**
     * Gets the item's enchantments.
     * @return the item's enchantments
     */
    @NonNull EnchantmentInformation[] getEnchantments();

    /**
     * Gets a list of enchantments that can be applied to the item, this can be used if the base material doesn't allow
     * the enchantment by default.
     * @return an array of enchantments that can be applied to the item
     */
    @Nullable Enchantment[] getAllowedEnchantments();

    /**
     * Gets a list of enchantments that can't be applied to the item, this can be used if the base material allows
     * the enchantment by default.
     * @return an array of enchantments that can't be applied to the item
     */
    @Nullable Enchantment[] getForbiddenEnchantments();

    /**
     * Gets if the item can be enchanted with the specified enchantment. This depends on the enchantments that are set
     * in {@link #getAllowedEnchantments()} and {@link #getForbiddenEnchantments()}.
     * @param enchantment the enchantment to check
     * @return if the item can be enchanted with the specified enchantment
     */
    @NonNull TriState canEnchant(@NonNull Enchantment enchantment);

    /**
     * Gets the item stack of the item.
     * @return the item stack of the item
     */
    @NonNull ItemStack getItemStack();

    /**
     * This class represents the information about an attribute modifier.
     */
    record AttributeInformation(@NonNull Attribute attribute, @NonNull AttributeModifier[] modifiers) {
    }

    /**
     * This class represents the information about an enchantment.
     */
    record EnchantmentInformation(@NonNull Enchantment enchantment, int level) {
    }

    interface Builder extends GenericBuilder<CustomItem> {
        /**
         * Sets if the item is unbreakable.
         * @param unbreakable if the item is unbreakable
         * @return the builder
         */
        @NonNull Builder unbreakable(boolean unbreakable);

        /**
         * Sets if the item hides flags.
         * @param hideFlags if the item hides flags
         * @return the builder
         */
        @NonNull Builder hideFlags(boolean hideFlags);

        /**
         * Sets if the item is a new item or if it is an extension of an existing item.
         * @param newItem if the item is a new item
         * @return the builder
         */
        @NonNull Builder newItem(boolean newItem);

        /**
         * Sets the event listener of the item.
         * @param itemUseEvent the event listener of the item
         * @return the builder
         */
        @NonNull Builder itemUseEvent(@Nullable Consumer<PlayerInteractEvent> itemUseEvent);

        /**
         * Sets the event listener of the item. The classes specified here must have a constructor like this:
         * <pre>
         *     public MyListener(CustomItem customItem) {
         *     }
         * </pre>
         *
         * The use of this method is completely optional, you can just register the listener yourself with the usual API.
         *
         * @param eventListener the event listener of the item
         * @return the builder
         */
        @NonNull Builder eventListener(@Nullable Class<? extends Listener> eventListener);

        /**
         * Sets the item lore.
         * @param spacingBeforeLore if there should be spacing before the lore
         * @param lore the lore
         * @return the builder
         */
        @NonNull Builder lore(boolean spacingBeforeLore, String... lore);

        /**
         * Sets the item lore.
         * @param lore the lore
         * @return the builder
         */
        @NonNull Builder lore(@Nullable String... lore);

        /**
         * Adds an attribute modifier to the item.
         * @param attribute the attribute
         * @param attributeModifier the attribute modifier
         * @return the builder
         */
        @NonNull Builder attributeModifier(@NonNull Attribute attribute, @NonNull AttributeModifier attributeModifier);

        /**
         * Adds an enchantment to the item.
         * @param enchantment the enchantment
         * @param level the level
         * @return the builder
         */
        @NonNull Builder enchantment(@NonNull Enchantment enchantment, int level);

        /**
         * Adds an allowed enchantment to the item, this can be used if the base material doesn't allow the enchantment
         * by default.
         * @param enchantments the enchantments
         * @return the builder
         */
        @NonNull Builder allowedEnchantment(@NonNull Enchantment... enchantments) throws CustomItemException;

        /**
         * Adds a forbidden enchantment to the item, this can be used if the base material allows the enchantment
         * by default.
         * @param enchantments the enchantments
         * @return the builder
         */
        @NonNull Builder forbiddenEnchantment(@NonNull Enchantment... enchantments) throws CustomItemException;
    }
}
