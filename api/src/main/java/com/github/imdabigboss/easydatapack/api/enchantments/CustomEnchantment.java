package com.github.imdabigboss.easydatapack.api.enchantments;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import com.github.imdabigboss.easydatapack.api.utils.StringUtil;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class represents a custom enchantment.
 */
public class CustomEnchantment extends Enchantment {
    private final String name;
    private final String namespace;
    private final Predicate<ItemStack> canEnchantItem;
    private final EnchantmentTarget enchantmentTarget;
    private final int maxLevel;
    private final int startLevel;
    private final List<Enchantment> conflictList;
    private final boolean treasure;
    private final boolean cursed;
    private final boolean tradeable;
    private final boolean discoverable;
    private final EnchantmentRarity rarity;
    private final BiFunction<Integer, EntityCategory, Float> damageIncrease;
    private final Function<Integer, Integer> anvilMergeCost;
    private final Function<Integer, Integer> tradeCost;
    private final Class<? extends Listener> eventListener;

    private CustomEnchantment(String name, String namespace, Predicate<ItemStack> canEnchantItem, EnchantmentTarget enchantmentTarget, int maxLevel, int startLevel, List<Enchantment> conflictList, boolean treasure, boolean cursed, boolean tradeable, boolean discoverable, EnchantmentRarity rarity, BiFunction<Integer, EntityCategory, Float> damageIncrease, Function<Integer, Integer> anvilMergeCost, Function<Integer, Integer> tradeCost, Class<? extends Listener> eventListener) {
        super(new NamespacedKey(EasyDatapackAPI.NAMESPACE_KEY, namespace));

        this.name = name;
        this.namespace = namespace;
        this.maxLevel = maxLevel;
        this.startLevel = startLevel;
        this.conflictList = conflictList;
        this.canEnchantItem = canEnchantItem;
        this.enchantmentTarget = enchantmentTarget;
        this.treasure = treasure;
        this.cursed = cursed;
        this.tradeable = tradeable;
        this.discoverable = discoverable;
        this.rarity = rarity;
        this.damageIncrease = damageIncrease;
        this.anvilMergeCost = anvilMergeCost;
        this.tradeCost = tradeCost;
        this.eventListener = eventListener;
    }

    /**
     * Gets the name of the enchantment.
     * @return the name of the enchantment
     */
    @Override
    public @NonNull String getName() {
        return this.name;
    }

    /**
     * Gets the namespace key of the enchantment.
     * @return the namespace key of the enchantment
     */
    public @NonNull String getNamespace() {
        return this.namespace;
    }

    /**
     * Gets the maximum level of the enchantment.
     * @return the maximum level of the enchantment
     */
    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    /**
     * Gets the starting level, or the minimum of the enchantment.
     * @return the starting level of the enchantment
     */
    @Override
    public int getStartLevel() {
        return this.startLevel;
    }

    /**
     * Checks if the enchantment conflicts with another.
     * @param other the other enchantment to check
     * @return true if the enchantment conflicts, false otherwise
     */
    @Override
    public boolean conflictsWith(@NonNull Enchantment other) {
        return this.conflictList.contains(other);
    }

    /**
     * Checks if the enchantment can be applied to an item.
     * @param item the item to check
     * @return true if the enchantment can be applied, false otherwise
     */
    @Override
    public boolean canEnchantItem(@NonNull ItemStack item) {
        if (item.getType() == Material.AIR) {
            return false;
        }
        if (item.getType() == Material.ENCHANTED_BOOK || item.getType() == Material.BOOK) {
            return true;
        }

        return this.canEnchantItem.test(item);
    }

    /**
     * Gets the item types that the enchantment can be applied to.
     * @return the item types that the enchantment can be applied to
     */
    @Override
    public @NonNull EnchantmentTarget getItemTarget() {
        return this.enchantmentTarget;
    }

    /**
     * Gets if the enchantment is a treasure.
     * @return true if the enchantment is a treasure, false otherwise
     */
    @Override
    public boolean isTreasure() {
        return this.treasure;
    }

    /**
     * Gets if the enchantment is a curse.
     * @return true if the enchantment is a curse, false otherwise
     */
    @Override
    public boolean isCursed() {
        return this.cursed;
    }

    /**
     * Gets the enchantment's display name by level.
     * @param i the level of the enchantment
     * @return the enchantment's display name
     */
    @Override
    public @NonNull Component displayName(int i) {
        return Component.text(this.name);
    }

    /**
     * Gets if the enchantment is tradeable.
     * @return true if the enchantment is tradeable, false otherwise
     */
    @Override
    public boolean isTradeable() {
        return this.tradeable;
    }

    /**
     * Gets if the enchantment is discoverable.
     * @return true if the enchantment is discoverable, false otherwise
     */
    @Override
    public boolean isDiscoverable() {
        return this.discoverable;
    }

    /**
     * Gets the enchantment's rarity.
     * @return the enchantment's rarity
     */
    @Override
    public @NonNull EnchantmentRarity getRarity() {
        return this.rarity;
    }

    /**
     * Gets the encantment's damage increase by level and entity category.
     * @param level the level of the enchantment
     * @param entityCategory the entity category
     * @return the damage increase
     */
    @Override
    public float getDamageIncrease(int level, @NonNull EntityCategory entityCategory) {
        Float out = this.damageIncrease.apply(level, entityCategory);
        return out == null ? 0 : out;
    }

    /**
     * Gets the enchantment's active slots.
     * @return an empty set
     */
    @Override
    public @NonNull Set<EquipmentSlot> getActiveSlots() {
        return Set.of();
    }

    /**
     * Gets the enchantment's translation key.
     * @return the enchantment's translation key
     */
    @Override
    public @NonNull String translationKey() {
        return "enchantment." + this.namespace;
    }

    /**
     * Gets the enchantment's anvil merge cost by level.
     * @param level the level of the enchantment
     * @return the anvil merge cost
     */
    public int getAnvilMergeCost(int level) {
        Integer out = this.anvilMergeCost.apply(level);
        return out == null ? 0 : out;
    }

    /**
     * Gets the enchantment's trade cost by level.
     * @param level the level of the enchantment
     * @return the trade cost
     */
    public int getTradeCost(int level) {
        Integer out = this.tradeCost.apply(level);
        return out == null ? 0 : out;
    }

    /**
     * Gets the enchantment's event listener if it has one.
     * @return the enchantment's event listener
     */
    public @Nullable Class<? extends Listener> getEventListener() {
        return this.eventListener;
    }

    /**
     * Gets the formatted name of the enchantment by level. For example, "Sharpness V".
     * @param level the level of the enchantment
     * @return the formatted name of the enchantment
     */
    public @NonNull String formatEnchantmentName(int level) {
        if (this.getMaxLevel() == 1) {
            return this.getName();
        } else {
            return this.getName() + " " + StringUtil.toRoman(level);
        }
    }

    /**
     * Creates an item stack of a book with the enchantment.
     * @param level the level of the enchantment
     * @return the item stack of the book
     */
    public @NonNull ItemStack getBook(int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.GRAY + this.formatEnchantmentName(level)));
        meta.lore(lore);

        book.setItemMeta(meta);

        EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta) book.getItemMeta();
        enchMeta.addStoredEnchant(this, level, false);
        book.setItemMeta(enchMeta);

        return book;
    }

    /**
     * Checks if an item has the enchantment.
     * @param item the item to check
     * @return true if the item has the enchantment, false otherwise
     */
    public boolean hasEnchantment(@NonNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasEnchant(this);
    }

    /**
     * Gets the level of the enchantment on an item.
     * @param item the item to check
     * @return the level of the enchantment
     */
    public int getEnchantmentLevel(ItemStack item) {
        if (!this.hasEnchantment(item)) {
            return 0;
        }
        return item.getItemMeta().getEnchantLevel(this);
    }

    /**
     * This class represents a builder for creating custom enchantments.
     */
    public static class Builder {
        protected final String name;
        protected final String namespace;
        protected final Predicate<ItemStack> canEnchantItem;
        protected final EnchantmentTarget enchantmentTarget;

        protected int maxLevel = 1;
        protected int startLevel = 1;
        protected List<Enchantment> conflictList = new ArrayList<>();
        protected boolean treasure = false;
        protected boolean cursed = false;
        protected boolean tradeable = true;
        protected boolean discoverable = true;
        protected EnchantmentRarity rarity = EnchantmentRarity.COMMON;
        protected BiFunction<Integer, EntityCategory, Float> damageIncrease = (level, entityCategory) -> 0f;
        protected Function<Integer, Integer> anvilMergeCost = level -> 0;
        protected Function<Integer, Integer> tradeCost = level -> 0;
        protected Class<? extends Listener> eventListener = null;

        /**
         * Creates a new builder for creating custom enchantments.
         * @param name the name of the enchantment
         * @param namespace the namespace of the enchantment, a name in snake_case
         * @param canEnchantItem the predicate to check if an item can be enchanted
         * @param enchantmentTarget the enchantment target
         */
        public Builder(@NonNull String name, @NonNull String namespace, @NonNull Predicate<ItemStack> canEnchantItem, @NonNull EnchantmentTarget enchantmentTarget) {
            this.name = name;
            this.namespace = namespace;
            this.canEnchantItem = canEnchantItem;
            this.enchantmentTarget = enchantmentTarget;
        }

        /**
         * Sets the max level of the enchantment.
         * @param maxLevel the max level of the enchantment
         * @return the builder
         */
        public @NonNull Builder maxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }

        /**
         * Sets the start level, or the minimum level of the enchantment.
         * @param startLevel the start level of the enchantment
         * @return the builder
         */
        public @NonNull Builder startLevel(int startLevel) {
            this.startLevel = startLevel;
            return this;
        }

        /**
         * Adds a conflicting enchantment.
         * @param conflict the conflicting enchantment
         * @return the builder
         */
        public @NonNull Builder addConflict(Enchantment... conflict) {
            this.conflictList.addAll(List.of(conflict));
            return this;
        }

        /**
         * Sets if the enchantment is a treasure.
         * @param treasure true if the enchantment is treasure, false otherwise
         * @return the builder
         */
        public @NonNull Builder treasure(boolean treasure) {
            this.treasure = treasure;
            return this;
        }

        /**
         * Sets if the enchantment is a curse.
         * @param cursed true if the enchantment is a curse, false otherwise
         * @return the builder
         */
        public @NonNull Builder cursed(boolean cursed) {
            this.cursed = cursed;
            return this;
        }

        /**
         * Sets if the enchantment is tradeable.
         * @param tradeable true if the enchantment is tradeable, false otherwise
         * @return the builder
         */
        public @NonNull Builder tradeable(boolean tradeable) {
            this.tradeable = tradeable;
            return this;
        }

        /**
         * Sets if the enchantment is discoverable.
         * @param discoverable true if the enchantment is discoverable, false otherwise
         * @return the builder
         */
        public @NonNull Builder discoverable(boolean discoverable) {
            this.discoverable = discoverable;
            return this;
        }

        /**
         * Sets the rarity of the enchantment.
         * @param rarity the rarity of the enchantment
         * @return the builder
         */
        public @NonNull Builder rarity(@NonNull EnchantmentRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        /**
         * Sets the damage increase of the enchantment.
         * @param damageIncrease the damage increase bi-function of the enchantment
         * @return the builder
         */
        public @NonNull Builder damageIncrease(@NonNull BiFunction<Integer, EntityCategory, Float> damageIncrease) {
            this.damageIncrease = damageIncrease;
            return this;
        }

        /**
         * Sets the anvil merge cost of the enchantment.
         * @param anvilMergeCost the anvil merge cost function of the enchantment
         * @return the builder
         */
        public @NonNull Builder anvilMergeCost(@NonNull Function<Integer, Integer> anvilMergeCost) {
            this.anvilMergeCost = anvilMergeCost;
            return this;
        }

        /**
         * Sets the trade cost of the enchantment.
         * @param tradeCost the trade cost function of the enchantment
         * @return the builder
         */
        public @NonNull Builder tradeCost(@NonNull Function<Integer, Integer> tradeCost) {
            this.tradeCost = tradeCost;
            return this;
        }

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
        public @NonNull Builder eventListener(@Nullable Class<? extends Listener> eventListener) {
            this.eventListener = eventListener;
            return this;
        }

        /**
         * Builds the enchantment.
         * @return the enchantment
         */
        public @NonNull CustomEnchantment build() {
            return new CustomEnchantment(this.name, this.namespace, this.canEnchantItem, this.enchantmentTarget, this.maxLevel, this.startLevel, this.conflictList, this.treasure, this.cursed, this.tradeable, this.discoverable, this.rarity, this.damageIncrease, this.anvilMergeCost, this.tradeCost, this.eventListener);
        }
    }
}
