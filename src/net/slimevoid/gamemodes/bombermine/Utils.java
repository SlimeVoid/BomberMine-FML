package net.slimevoid.gamemodes.bombermine;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.ServerConfigurationManager;

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
		renderer.func_83020_a(minX, minY, minZ, maxX, maxY, maxZ);
	}
}
