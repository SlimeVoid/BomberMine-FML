package net.slimevoid.gamemodes.bombermine;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

public class Utils {
	
	public static RenderBlocks renderer;
	
	public static void sendChatToAll(String str) {
		ServerConfigurationManager conf = MinecraftServer.getServer().getConfigurationManager();
		conf.sendPacketToAllPlayers(new Packet3Chat(str));
	}
	
	public static void sendPacketToAll(Packet packet) {
		ServerConfigurationManager conf = MinecraftServer.getServer().getConfigurationManager();
		conf.sendPacketToAllPlayers(packet);
	}
	
	public static void setRendererBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
