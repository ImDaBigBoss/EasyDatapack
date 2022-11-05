package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Some utilities for blocks.
 */
public class BlockUtil {
    /**
     * A list of the blocks that shouldn't be broken by the player in survival mode.
     */
    public static final @NonNull List<Material> UNBREAKABLE_BLOCKS = List.of(Material.BEDROCK, Material.BARRIER, Material.NETHER_PORTAL, Material.END_PORTAL_FRAME, Material.END_PORTAL, Material.END_GATEWAY);

    /**
     * Breaks a block as if a player broke it: correct drops & event firing.
     * @param server the server
     * @param player the player
     * @param block the block to break
     * @param item the item used to break the block
     * @return true if the block was broken, false otherwise
     */
    public static boolean breakBlock(@NonNull Server server, @NonNull Player player, @NonNull Block block, @NonNull ItemStack item) {
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        if (event.isDropItems() && player.getGameMode() != GameMode.CREATIVE) {
            block.breakNaturally(item);
        } else {
            block.setType(Material.AIR);
        }

        return true;
    }
}
