package com.github.imdabigboss.easydatapack.api.types.items;

import com.github.imdabigboss.easydatapack.api.EasyDatapackAPI;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;
import java.util.function.Consumer;

public interface CustomToolItem extends CustomItem {
    /**
     * Creates a new tool builder.
     * @param customModelData the custom model data of the item
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     * @param attackDamage the attack damage that the tool deals
     * @param attackSpeed the attack speed of the tool
     * @return the builder
     */
    static CustomToolItem.ToolBuilder builder(int customModelData, @NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, double attackDamage, double attackSpeed) {
        return (CustomToolItem.ToolBuilder) EasyDatapackAPI.createBuilder(ToolBuilder.class, customModelData, namespaceKey, name, baseMaterial, attackDamage, attackSpeed);
    }

    /**
     * Creates a new tool builder.
     * @param namespaceKey the namespace key of the item
     * @param name the name of the item
     * @param baseMaterial the base material of the item
     * @param texture the path to the item texture to register
     * @param attackDamage the attack damage that the tool deals
     * @param attackSpeed the attack speed of the tool
     * @return the builder
     */
    static CustomToolItem.ToolBuilder builder(@NonNull String namespaceKey, @NonNull String name, @NonNull Material baseMaterial, @NonNull Path texture, double attackDamage, double attackSpeed) {
        int customModelData = EasyDatapackAPI.getTexturePackManager().registerItemTexture(baseMaterial, true, false, texture);
        return builder(customModelData, namespaceKey, name, baseMaterial, attackDamage, attackSpeed);
    }

    /**
     * Gets the attack damage that the tool deals.
     * @return the attack damage that the tool deals
     */
    double getAttackDamage();

    /**
     * Gets the attack speed of the tool.
     * @return the attack speed of the tool
     */
    double getAttackSpeed();

    /**
     * Gets the event that is called when a player hits an entity with the tool.
     * @return the event that is called when a player hits an entity with the tool
     */
    @Nullable Consumer<EntityDamageByEntityEvent> getPlayerHitEntityEvent();

    /**
     * Gets the event that is called when a player breaks a block with the tool.
     * @return the event that is called when a player breaks a block with the tool
     */
    @Nullable Consumer<BlockBreakEvent> getPlayerBreakBlockEvent();

    /**
     * Reformat the lore of the item.
     */
    void reformatLore(@NonNull ItemStack itemStack);

    /**
     * This class represents a builder for a custom tool item.
     */
    interface ToolBuilder extends Builder {
        /**
         * Sets the event that is called when a player hits an entity with the tool.
         * @param playerHitEntityEvent the event that is called when a player hits an entity with the tool
         * @return the builder
         */
        @NonNull ToolBuilder playerHitEntityEvent(@Nullable Consumer<EntityDamageByEntityEvent> playerHitEntityEvent);

        /**
         * Sets the event that is called when a player breaks a block with the tool.
         * @param playerBreakBlockEvent the event that is called when a player breaks a block with the tool
         * @return the builder
         */
        @NonNull ToolBuilder playerBreakBlockEvent(@Nullable Consumer<BlockBreakEvent> playerBreakBlockEvent);
    }
}
