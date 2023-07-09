package me.lostedark.aetherplugins.scoreboard.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBarUtil {
   public static void sendActionBar(Player player, String message) {
      PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte)2);
      ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
   }
}
