package com.github.imdabigboss.easydatapack.api.utils;

import org.bukkit.entity.Player;

public class PacketUtil {
    /**
     * @deprecated Use {@link Player#swingMainHand()} instead.
     * Sends the player an arm animation packet.
     * @param player The player to send the packet to.
     */
    public void sendPlayerArmAnimation(Player player) {
        player.swingMainHand();
    }
}
