package com.github.imdabigboss.easydatapack.backend.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.github.imdabigboss.easydatapack.api.utils.PacketUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PacketUtilImpl implements PacketUtil {
    private final ProtocolManager protocolManager;

    public PacketUtilImpl() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void sendPlayerArmAnimation(Player player) {
        PacketContainer animation = this.protocolManager.createPacket(PacketType.Play.Server.ANIMATION, false);
        animation.getEntityModifier(player.getWorld()).write(0, player);
        animation.getIntegers().write(1, 0);
        try {
            this.protocolManager.sendServerPacket(player, animation);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
