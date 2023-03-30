package com.github.imdabigboss.easydatapack.backend.items;

import com.github.imdabigboss.easydatapack.api.items.CustomToolItem;
import com.github.imdabigboss.easydatapack.backend.utils.GenericBuilderImpl;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class CustomToolItemImpl extends CustomItemImpl implements CustomToolItem {
    private final double attackDamage;
    private final double attackSpeed;
    private final Consumer<EntityDamageByEntityEvent> playerHitEntityEvent;
    private final Consumer<BlockBreakEvent> playerBreakBlockEvent;

    private CustomToolItemImpl(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, boolean unbreakable, boolean hideFlags, double attackDamage, double attackSpeed, @Nullable Class<? extends Listener> eventListener, @Nullable Consumer<PlayerInteractEvent> itemUseEvent, @Nullable Consumer<EntityDamageByEntityEvent> playerHitEntityEvent, @Nullable Consumer<BlockBreakEvent> playerBreakBlockEvent, boolean spacingBeforeLore, @Nullable String[] lore, @NonNull Map<Attribute, List<AttributeModifier>> attributeModifiers, @NonNull Map<Enchantment, Integer> enchantments, @NonNull List<Enchantment> allowedEnchantments, @NonNull List<Enchantment> forbiddenEnchantments) {
        super(customModelData, namespaceKey, name, baseMaterial, unbreakable, hideFlags, true, eventListener, itemUseEvent, spacingBeforeLore, lore, attributeModifiers, enchantments, allowedEnchantments, forbiddenEnchantments);

        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.playerHitEntityEvent = playerHitEntityEvent;
        this.playerBreakBlockEvent = playerBreakBlockEvent;

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", attackDamage - 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", (4 - attackSpeed) * -1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));

        this.itemStack.setItemMeta(itemMeta);

        formatToolPropertiesLore(this.itemStack, this);
    }

    @Override
    public double getAttackDamage() {
        return attackDamage;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public @Nullable Consumer<EntityDamageByEntityEvent> getPlayerHitEntityEvent() {
        return playerHitEntityEvent;
    }

    @Override
    public @Nullable Consumer<BlockBreakEvent> getPlayerBreakBlockEvent() {
        return playerBreakBlockEvent;
    }

    @Override
    public void reformatLore(@NonNull ItemStack itemStack) {
        formatToolPropertiesLore(itemStack, this);
    }

    static void formatToolPropertiesLore(ItemStack itemStack, CustomToolItem customItem) {
        if (!itemStack.hasItemMeta()) {
            return;
        }

        if (!customItem.isHideFlags()) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            double sharpnessExtra = 0;
            if (itemMeta.hasEnchant(Enchantment.DAMAGE_ALL)) {
                int level = itemMeta.getEnchantLevel(Enchantment.DAMAGE_ALL);
                sharpnessExtra = 0.5 * Math.max(0, level - 1) + 1.0;
            }

            String stringAttackDamage = " " + (customItem.getAttackDamage() + sharpnessExtra);
            if (stringAttackDamage.endsWith(".0")) {
                stringAttackDamage = stringAttackDamage.substring(0, stringAttackDamage.length() - 2);
            }
            String stringAttackSpeed = " " + customItem.getAttackSpeed();
            if (stringAttackSpeed.endsWith(".0")) {
                stringAttackSpeed = stringAttackSpeed.substring(0, stringAttackSpeed.length() - 2);
            }

            List<String> loreList;
            if (itemMeta.lore() != null) {
                loreList = itemMeta.getLore();
            } else {
                loreList = new ArrayList<>();
            }

            String whenInMainHand = ChatColor.GRAY + "When in Main Hand:";
            String attackDamage = ChatColor.DARK_GREEN + stringAttackDamage + " Attack Damage";
            String attackSpeed = ChatColor.DARK_GREEN + stringAttackSpeed + " Attack Speed";

            if (loreList.contains(whenInMainHand)) {
                int whenInMainHandIndex = loreList.indexOf(whenInMainHand);

                loreList.remove(loreList.indexOf(whenInMainHand) + 1);
                loreList.remove(loreList.indexOf(whenInMainHand) + 1);

                loreList.add(whenInMainHandIndex + 1, attackDamage);
                loreList.add(whenInMainHandIndex + 2, attackSpeed);
            } else {
                loreList.add("");
                loreList.add(whenInMainHand);
                loreList.add(attackDamage);
                loreList.add(attackSpeed);
            }

            itemMeta.setLore(loreList);

            itemStack.setItemMeta(itemMeta);
        }
    }

    public static class ToolBuilderImpl extends BuilderImpl implements ToolBuilder, GenericBuilderImpl {
        private final double attackDamage;
        private final double attackSpeed;

        private Consumer<EntityDamageByEntityEvent> playerHitEntityEvent = null;
        private Consumer<BlockBreakEvent> playerBreakBlockEvent = null;

        public ToolBuilderImpl(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, double attackDamage, double attackSpeed) {
            super(customModelData, namespaceKey, name, baseMaterial);

            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
        }

        @Override
        public @NonNull ToolBuilder playerHitEntityEvent(@Nullable Consumer<EntityDamageByEntityEvent> playerHitEntityEvent) {
            this.playerHitEntityEvent = playerHitEntityEvent;
            return this;
        }

        @Override
        public @NonNull ToolBuilder playerBreakBlockEvent(@Nullable Consumer<BlockBreakEvent> playerBreakBlockEvent) {
            this.playerBreakBlockEvent = playerBreakBlockEvent;
            return this;
        }

        @Override
        public @NonNull CustomToolItemImpl build() {
            return new CustomToolItemImpl(this.customModelData, this.namespaceKey, this.name, this.baseMaterial, this.unbreakable, this.hideFlags, this.attackDamage, this.attackSpeed, this.eventListener, this.itemUseEvent, this.playerHitEntityEvent, this.playerBreakBlockEvent, this.spacingBeforeLore, this.lore, this.attributeModifiers, this.enchantments, this.allowedEnchantments, this.forbiddenEnchantments);
        }
    }
}
