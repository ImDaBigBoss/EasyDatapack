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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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

    @Override
    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getStartLevel() {
        return this.startLevel;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return this.conflictList.contains(other);
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        if (item.getType() == Material.AIR) {
            return false;
        }
        if (item.getType() == Material.ENCHANTED_BOOK || item.getType() == Material.BOOK) {
            return true;
        }

        return this.canEnchantItem.test(item);
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return this.enchantmentTarget;
    }

    @Override
    public boolean isTreasure() {
        return this.treasure;
    }

    @Override
    public boolean isCursed() {
        return this.cursed;
    }

    @Override
    public @NotNull Component displayName(int i) {
        return Component.text(this.name);
    }

    @Override
    public boolean isTradeable() {
        return this.tradeable;
    }

    @Override
    public boolean isDiscoverable() {
        return this.discoverable;
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return this.rarity;
    }

    @Override
    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        Float out = this.damageIncrease.apply(level, entityCategory);
        return out == null ? 0 : out;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return Set.of();
    }

    @Override
    public @NotNull String translationKey() {
        return "enchantment." + this.namespace + "." + this.name;
    }

    public int getAnvilMergeCost(int level) {
        Integer out = this.anvilMergeCost.apply(level);
        return out == null ? 0 : out;
    }

    public int getTradeCost(int level) {
        Integer out = this.tradeCost.apply(level);
        return out == null ? 0 : out;
    }

    public Class<? extends Listener> getEventListener() {
        return this.eventListener;
    }

    public String formatEnchantmentName(int level) {
        if (this.getMaxLevel() == 1) {
            return this.getName();
        } else {
            return this.getName() + " " + StringUtil.toRoman(level);
        }
    }

    public ItemStack getBook(int level) {
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

    public boolean hasEnchantment(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasEnchant(this);
    }

    public int getEnchantmentLevel(ItemStack item) {
        if (!this.hasEnchantment(item)) {
            return 0;
        }
        return item.getItemMeta().getEnchantLevel(this);
    }

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

        public Builder(String name, String namespace, Predicate<ItemStack> canEnchantItem, EnchantmentTarget enchantmentTarget) {
            this.name = name;
            this.namespace = namespace;
            this.canEnchantItem = canEnchantItem;
            this.enchantmentTarget = enchantmentTarget;
        }

        public Builder maxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }

        public Builder startLevel(int startLevel) {
            this.startLevel = startLevel;
            return this;
        }

        public Builder addConflict(Enchantment... conflict) {
            this.conflictList.addAll(List.of(conflict));
            return this;
        }

        public Builder treasure(boolean treasure) {
            this.treasure = treasure;
            return this;
        }

        public Builder cursed(boolean cursed) {
            this.cursed = cursed;
            return this;
        }

        public Builder tradeable(boolean tradeable) {
            this.tradeable = tradeable;
            return this;
        }

        public Builder discoverable(boolean discoverable) {
            this.discoverable = discoverable;
            return this;
        }

        public Builder rarity(EnchantmentRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder damageIncrease(BiFunction<Integer, EntityCategory, Float> damageIncrease) {
            this.damageIncrease = damageIncrease;
            return this;
        }

        public Builder anvilMergeCost(Function<Integer, Integer> anvilMergeCost) {
            this.anvilMergeCost = anvilMergeCost;
            return this;
        }

        public Builder tradeCost(Function<Integer, Integer> tradeCost) {
            this.tradeCost = tradeCost;
            return this;
        }

        public Builder eventListener(Class<? extends Listener> eventListener) {
            this.eventListener = eventListener;
            return this;
        }

        public CustomEnchantment build() {
            return new CustomEnchantment(this.name, this.namespace, this.canEnchantItem, this.enchantmentTarget, this.maxLevel, this.startLevel, this.conflictList, this.treasure, this.cursed, this.tradeable, this.discoverable, this.rarity, this.damageIncrease, this.anvilMergeCost, this.tradeCost, this.eventListener);
        }
    }
}
