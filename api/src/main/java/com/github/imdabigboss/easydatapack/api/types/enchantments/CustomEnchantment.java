package com.github.imdabigboss.easydatapack.api.types.enchantments;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.utils.GenericBuilder;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class represents a custom enchantment.
 */
public interface CustomEnchantment {
    /**
     * Creates a new builder for creating custom enchantments.
     * @param name the name of the enchantment
     * @param namespace the namespace of the enchantment, a name in snake_case
     * @param canEnchantItem the predicate to check if an item can be enchanted
     * @param enchantmentTarget the enchantment target
     */
    static Builder builder(@NonNull String name, @NonNull String namespace, @NonNull Predicate<ItemStack> canEnchantItem, @NonNull EnchantmentTarget enchantmentTarget) {
        return (Builder) EasyDatapackAPI.createBuilder(Builder.class, name, namespace, canEnchantItem, enchantmentTarget);
    }

    /**
     * Gets the name of the enchantment.
     * @return the name of the enchantment
     */
    @NonNull String getName();

    /**
     * Gets the namespace key of the enchantment.
     * @return the namespace key of the enchantment
     */
    @NonNull String getNamespace();

    /**
     * Gets this enchantment as a Bukkit enchantment.
     * @return this enchantment as a Bukkit enchantment
     */
    @NonNull Enchantment asEnchantment();

    /**
     * Gets the maximum level of the enchantment.
     * @return the maximum level of the enchantment
     */
    int getMaxLevel();

    /**
     * Gets the starting level, or the minimum of the enchantment.
     * @return the starting level of the enchantment
     */
    int getStartLevel();

    /**
     * Checks if the enchantment conflicts with another.
     * @param other the other enchantment to check
     * @return true if the enchantment conflicts, false otherwise
     */
    boolean conflictsWith(@NonNull Enchantment other);

    /**
     * Checks if the enchantment can be applied to an item.
     * @param item the item to check
     * @return true if the enchantment can be applied, false otherwise
     */
    boolean canEnchantItem(@NonNull ItemStack item);

    /**
     * Gets the item types that the enchantment can be applied to.
     * @return the item types that the enchantment can be applied to
     */
    @NonNull EnchantmentTarget getItemTarget();

    /**
     * Gets if the enchantment is a treasure.
     * @return true if the enchantment is a treasure, false otherwise
     */
    boolean isTreasure();

    /**
     * Gets if the enchantment is a curse.
     * @return true if the enchantment is a curse, false otherwise
     */
    boolean isCursed();

    /**
     * Gets the enchantment's display name by level.
     * @param i the level of the enchantment
     * @return the enchantment's display name
     */
    @NonNull Component displayName(int i);

    /**
     * Gets if the enchantment is tradeable.
     * @return true if the enchantment is tradeable, false otherwise
     */
    boolean isTradeable();

    /**
     * Gets if the enchantment is discoverable.
     * @return true if the enchantment is discoverable, false otherwise
     */
    boolean isDiscoverable();

    /**
     * Gets the enchantment's rarity.
     * @return the enchantment's rarity
     */
    @NonNull EnchantmentRarity getRarity();

    /**
     * Gets the encantment's damage increase by level and entity category.
     * @param level the level of the enchantment
     * @param entityCategory the entity category
     * @return the damage increase
     */
    float getDamageIncrease(int level, @NonNull EntityCategory entityCategory);

    /**
     * Gets the enchantment's active slots.
     * @return an empty set
     */
    @NonNull Set<EquipmentSlot> getActiveSlots();

    /**
     * Gets the enchantment's translation key.
     * @return the enchantment's translation key
     */
    @NonNull String translationKey();

    /**
     * Gets the enchantment's anvil merge cost by level.
     * @param level the level of the enchantment
     * @return the anvil merge cost
     */
    int getAnvilMergeCost(int level);

    /**
     * Gets the enchantment's trade cost by level.
     * @param level the level of the enchantment
     * @return the trade cost
     */
    int getTradeCost(int level);

    /**
     * Gets the enchantment's event listener if it has one.
     * @return the enchantment's event listener
     */
    @Nullable Class<? extends Listener> getEventListener();

    /**
     * Gets the formatted name of the enchantment by level. For example, "Sharpness V".
     * @param level the level of the enchantment
     * @return the formatted name of the enchantment
     */
    @NonNull String formatEnchantmentName(int level);

    /**
     * Creates an item stack of a book with the enchantment.
     * @param level the level of the enchantment
     * @return the item stack of the book
     */
    @NonNull ItemStack getBook(int level);

    /**
     * Checks if an item has the enchantment.
     * @param item the item to check
     * @return true if the item has the enchantment, false otherwise
     */
    boolean hasEnchantment(@NonNull ItemStack item);

    /**
     * Gets the level of the enchantment on an item.
     * @param item the item to check
     * @return the level of the enchantment
     */
    int getEnchantmentLevel(@NonNull ItemStack item);

    /**
     * This class represents a builder for creating custom enchantments.
     */
    interface Builder extends GenericBuilder<CustomEnchantment> {
        /**
         * Sets the max level of the enchantment.
         * @param maxLevel the max level of the enchantment
         * @return the builder
         */
        @NonNull Builder maxLevel(int maxLevel);

        /**
         * Sets the start level, or the minimum level of the enchantment.
         * @param startLevel the start level of the enchantment
         * @return the builder
         */
        @NonNull Builder startLevel(int startLevel);

        /**
         * Adds a conflicting enchantment.
         * @param conflict the conflicting enchantment
         * @return the builder
         */
        @NonNull Builder addConflict(Enchantment... conflict);

        /**
         * Sets if the enchantment is a treasure.
         * @param treasure true if the enchantment is treasure, false otherwise
         * @return the builder
         */
        @NonNull Builder treasure(boolean treasure);

        /**
         * Sets if the enchantment is a curse.
         * @param cursed true if the enchantment is a curse, false otherwise
         * @return the builder
         */
        @NonNull Builder cursed(boolean cursed);

        /**
         * Sets if the enchantment is tradeable.
         * @param tradeable true if the enchantment is tradeable, false otherwise
         * @return the builder
         */
        @NonNull Builder tradeable(boolean tradeable);

        /**
         * Sets if the enchantment is discoverable.
         * @param discoverable true if the enchantment is discoverable, false otherwise
         * @return the builder
         */
        @NonNull Builder discoverable(boolean discoverable);

        /**
         * Sets the rarity of the enchantment.
         * @param rarity the rarity of the enchantment
         * @return the builder
         */
        @NonNull Builder rarity(@NonNull EnchantmentRarity rarity);

        /**
         * Sets the damage increase of the enchantment.
         * @param damageIncrease the damage increase bi-function of the enchantment
         * @return the builder
         */
        @NonNull Builder damageIncrease(@NonNull BiFunction<Integer, EntityCategory, Float> damageIncrease);

        /**
         * Sets the anvil merge cost of the enchantment.
         * @param anvilMergeCost the anvil merge cost function of the enchantment
         * @return the builder
         */
        @NonNull Builder anvilMergeCost(@NonNull Function<Integer, Integer> anvilMergeCost);

        /**
         * Sets the trade cost of the enchantment.
         * @param tradeCost the trade cost function of the enchantment
         * @return the builder
         */
        @NonNull Builder tradeCost(@NonNull Function<Integer, Integer> tradeCost);

        /**
         * Sets the event listener of the enchantment. The classes specified here must have a constructor like this:
         * <pre>
         *     public MyListener(CustomEnchantment customItem) {
         *     }
         * </pre>
         *
         * The use of this method is completely optional, you can just register the listener yourself with the usual API.
         *
         * @param eventListener the event listener class of the enchantment, or null if there is no event listener
         * @return the builder
         */
        @NonNull Builder eventListener(@Nullable Class<? extends Listener> eventListener);
    }
}
