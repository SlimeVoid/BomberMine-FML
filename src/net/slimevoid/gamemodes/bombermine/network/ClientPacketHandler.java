package net.slimevoid.gamemodes.bombermine.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.slimevoid.gamemodes.bombermine.BomberMineFML;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBomb;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler extends CommonPacketHandler {
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
//		EntityPlayer entityplayer = (EntityPlayer) player;
//		World worldServer = entityplayer.worldObj;
		World worldClient = Minecraft.getMinecraft().theWorld;
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data));
		try {
			PacketType type = PacketType.values()[in.read()];
			switch(type) {
			case BOMB_EXPLOSION:
				int id = in.readInt();
				int x = in.readInt();
				int y = in.readInt();
				int z = in.readInt();
				int rayon = in.readInt();
				boolean passTroughWall = in.readBoolean();
				if(Block.blocksList[id] != null && Block.blocksList[id] instanceof BlockBomb) {
					((BlockBomb)Block.blocksList[id]).explodeClient(worldClient, x, y, z, rayon, passTroughWall);
				} else {
					BomberMineFML.instance.BOMB.explodeClient(worldClient, x, y, z, rayon, passTroughWall);
				}
				break;
				
			case PLAYER_SCORE:
				BomberMineFML.instance.scores.put(in.readUTF(), in.readInt());
				NetClientHandler netclienthandler = ((EntityClientPlayerMP)Minecraft.getMinecraft().thePlayer).sendQueue;
				List<GuiPlayerInfo> list = netclienthandler.playerInfoList;
				Collections.sort(list, new Comparator<GuiPlayerInfo>() {
					@Override
					public int compare(GuiPlayerInfo o1, GuiPlayerInfo o2) {
						return BomberMineFML.instance.getScore(o2.name) - BomberMineFML.instance.getScore(o1.name);
					}
				});
				break;
				
			case PLAYER_DIED:
				
				break;
				
			case MAP_INFO:
				
				break;
				
			case ROUND_STARTED:
				
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
