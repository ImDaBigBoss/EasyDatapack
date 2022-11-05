package com.github.imdabigboss.easydatapack.api.items;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class CustomToolItem extends CustomItem {
    private final double attackDamage;
    private final double attackSpeed;
    private final Consumer<EntityDamageByEntityEvent> playerHitEntityEvent;
    private final Consumer<BlockBreakEvent> playerBreakBlockEvent;

    private CustomToolItem(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, boolean unbreakable, boolean hideFlags, double attackDamage, double attackSpeed, @Nullable Class<? extends Listener> eventListener, @Nullable Consumer<PlayerInteractEvent> itemUseEvent, @Nullable Consumer<EntityDamageByEntityEvent> playerHitEntityEvent, @Nullable Consumer<BlockBreakEvent> playerBreakBlockEvent, boolean spacingBeforeLore, @Nullable String[] lore, @NonNull Map<Attribute, List<AttributeModifier>> attributeModifiers, @NonNull Map<Enchantment, Integer> enchantments) {
        super(customModelData, namespaceKey, name, baseMaterial, unbreakable, hideFlags, true, eventListener, itemUseEvent, spacingBeforeLore, lore, attributeModifiers, enchantments);

        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.playerHitEntityEvent = playerHitEntityEvent;
        this.playerBreakBlockEvent = playerBreakBlockEvent;

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", attackDamage - 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", (4 - attackSpeed) * -1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        if (!hideFlags) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            String stringAttackDamage = " " + attackDamage + "";
            if (stringAttackDamage.endsWith(".0")) {
                stringAttackDamage = stringAttackDamage.substring(0, stringAttackDamage.length() - 2);
            }
            String stringAttackSpeed = " " + attackSpeed + "";
            if (stringAttackSpeed.endsWith(".0")) {
                stringAttackSpeed = stringAttackSpeed.substring(0, stringAttackSpeed.length() - 2);
            }

            List<Component> loreList;
            if (itemMeta.lore() != null) {
                loreList = itemMeta.lore();
            } else {
                loreList = new ArrayList<>();
            }
            loreList.add(Component.text(""));
            loreList.add(Component.text(ChatColor.GRAY + "When in Main Hand:"));
            loreList.add(Component.text(ChatColor.DARK_GREEN + stringAttackDamage + " Attack Damage"));
            loreList.add(Component.text(ChatColor.DARK_GREEN + stringAttackSpeed + " Attack Speed"));

            itemMeta.lore(loreList);
        }

        this.itemStack.setItemMeta(itemMeta);
    }

    /**
     * Gets the attack damage of the tool.
     * @return the attack damage of the tool
     */
    public double getAttackDamage() {
        return attackDamage;
    }

    /**
     * Gets the attack speed of the tool.
     * @return the attack speed of the tool
     */
    public double getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Gets the event that is called when a player hits an entity with the tool.
     * @return the event that is called when a player hits an entity with the tool
     */
    public @Nullable Consumer<EntityDamageByEntityEvent> getPlayerHitEntityEvent() {
        return playerHitEntityEvent;
    }

    /**
     * Gets the event that is called when a player breaks a block with the tool.
     * @return the event that is called when a player breaks a block with the tool
     */
    public @Nullable Consumer<BlockBreakEvent> getPlayerBreakBlockEvent() {
        return playerBreakBlockEvent;
    }

    /**
     * This class represents a builder for a custom tool item.
     */
    public static class Builder extends CustomItem.Builder {
        private final double attackDamage;
        private final double attackSpeed;

        private Consumer<EntityDamageByEntityEvent> playerHitEntityEvent = null;
        private Consumer<BlockBreakEvent> playerBreakBlockEvent = null;

        /**
         * Creates a new builder for a custom tool item.
         * @param customModelData the custom model data of the item
         * @param namespaceKey the namespace key of the item
         * @param name the name of the item
         * @param baseMaterial the base material of the item
         * @param attackDamage the attack damage of the tool
         * @param attackSpeed the attack speed of the tool
         */
        public Builder(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, double attackDamage, double attackSpeed) {
            super(customModelData, namespaceKey, name, baseMaterial);

            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
        }

        /**
         * Sets the event that is called when a player hits an entity with the tool.
         * @param playerHitEntityEvent the event that is called when a player hits an entity with the tool
         * @return the builder
         */
        public @NonNull Builder playerHitEntityEvent(@Nullable Consumer<EntityDamageByEntityEvent> playerHitEntityEvent) {
            this.playerHitEntityEvent = playerHitEntityEvent;
            return this;
        }

        /**
         * Sets the event that is called when a player breaks a block with the tool.
         * @param playerBreakBlockEvent the event that is called when a player breaks a block with the tool
         * @return the builder
         */
        public @NonNull Builder playerBreakBlockEvent(@Nullable Consumer<BlockBreakEvent> playerBreakBlockEvent) {
            this.playerBreakBlockEvent = playerBreakBlockEvent;
            return this;
        }

        /**
         * Builds the custom tool item.
         * @return the custom tool item
         */
        @Override
        public @NonNull CustomToolItem build() {
            return new CustomToolItem(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.unbreakable, this.hideFlags, this.attackDamage, this.attackSpeed, this.eventListener, this.itemUseEvent, this.playerHitEntityEvent, this.playerBreakBlockEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments);
        }
    }
}
