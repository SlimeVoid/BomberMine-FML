package net.slimevoid.gamemodes.bombermine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBomb;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBonus;
import net.slimevoid.gamemodes.bombermine.blocks.BlockFireBomber;
import net.slimevoid.gamemodes.bombermine.blocks.BlockInvisible;
import net.slimevoid.gamemodes.bombermine.blocks.BlockPodium;
import net.slimevoid.gamemodes.bombermine.network.ClientPacketHandler;
import net.slimevoid.gamemodes.bombermine.network.CommonPacketHandler;
import net.slimevoid.gamemodes.bombermine.network.ConnectionHandler;
import net.slimevoid.gamemodes.bombermine.proxy.CommonProxy;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "bomberMine", name = "BomberMine", version = "0")
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = true,
		clientPacketHandlerSpec = @SidedPacketHandler(
				channels = { "BOMBERMINE" },
				packetHandler = ClientPacketHandler.class),
		serverPacketHandlerSpec = @SidedPacketHandler(
				channels = { "BOMBERMINE" },
				packetHandler = CommonPacketHandler.class),
		connectionHandler = ConnectionHandler.class)
public class BomberMineFML {
	
	public static BomberMineFML instance;
	public BlockBomb BOMB;
	public BlockBomb BOMB_LASER;
	public BlockBomb BOMB_ICE;
	public BlockBomb BOMB_FIRE;
	public BlockBomb BOMB_COBWEB;
	public BlockFireBomber FIRE;
	public BlockInvisible INVISIBLE;
	public BlockPodium PODUIM;
	public BlockBonus BONUS;
	public Map<String, Integer> scores;
	private String mapName;
	private String mapAuthor;
	private String mapComment;
	private long timeEndingBuildingMap;
	private long timeEndingRound;
	
	@SidedProxy(clientSide = "net.slimevoid.gamemodes.bombermine.proxy.ClientProxy", 
				serverSide = "net.slimevoid.gamemodes.bombermine.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static int preStartLenght = 400;
	public static int roundTimeLimit = 6000;
	public static int ptsForKill = 100;
	public static int ptsForSuicide = 400;
	public static String ignoredMaps = "";
	public static int mapChangeMode = 0;
	
//	@MLProp(info = "Amount of time in ticks (50ms) when the map is building (400 ticks = 20secs)", max = 6000, min = 0, name = "PreStartLenght") 	public static int preStartLenght = 400;
//	@MLProp(info = "Time limmit per round in ticks (50ms)", max = 120000, min = 0, name = "RoundTimeLimmit") 										public static int roundTimeLimmit = 6000;
//	@MLProp(info = "Amount of points give to the killer and removed from the victim", max = 10000, min = 0, name = "PtsForKill") 					public static int ptsForKill = 100;
//	@MLProp(info = "Amount of points removed for a suicide and shared between the alive players", max = 10000, min = 0, name = "PtsForSuicide") 	public static int ptsForSuicide = 400;
//	@MLProp(info = "Ignored maps, separated by \", \"", max = 0, min = 0, name = "IgnoredMaps") 													public static String ignoredMaps = "";
//	@MLProp(info = "How the map is changed (0 = random, 1 = normal (first map then second...))", max = 1, min = 0, name = "MapChangeMode") 			public static int mapChangeMode = 0;
	
	public BomberMineFML() {
		scores = new HashMap<>();
		mapName = "Map Name";
		mapAuthor = "Author";
		mapComment = "Comment";
	}
	
	@EventHandler
	public void BomberminePreInit(FMLPreInitializationEvent event) {
		instance = this;
		proxy.init();
	}

	@EventHandler
	public void BombermineInit(FMLInitializationEvent event) {
	}

	@EventHandler
	public void BomberminePostInit(FMLPostInitializationEvent event) {
	}
	
	public void init(FMLPreInitializationEvent e) {
		
		
		GameRegistry.registerWorldGenerator(new IWorldGenerator() {
			@Override
			public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
				Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
				byte[] ids = new byte[16 * 16 * world.getHeight()];
				for(int i = 0; i < ids.length; i ++) ids[i] = 0;
				chunk.fillChunk(ids, 16, 16, true);
			}
		});
		
	}

	public int getScore(EntityPlayer player) {
		return getScore(player.username);
	}
	
	public int getScore(String name) {
		if(scores.containsKey(name.toLowerCase())) {
			return scores.get(name.toLowerCase());
		}
		return -1;
	}
	
	public String getMapName() {
		return mapName;
	}

	public String getMapAuthor() {
		return mapAuthor;
	}

	public String getMapComment() {
		return mapComment;
	}
	
	public boolean isBuildingMap() {
		return timeEndingBuildingMap > System.currentTimeMillis();
	}
	
	public long getTimeUntilMapBuild() {
		return timeEndingBuildingMap - System.currentTimeMillis();
	}
	
	public boolean isRoundInProgress() {
		return timeEndingRound > System.currentTimeMillis();
	}

	public long getTimeUntilRoundEnded() {
		return timeEndingRound - System.currentTimeMillis();
	}
}
