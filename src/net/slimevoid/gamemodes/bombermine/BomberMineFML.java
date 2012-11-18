package net.slimevoid.gamemodes.bombermine;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MLProp;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBomb;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBombCobweb;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBombFire;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBombIce;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBombLaser;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBonus;
import net.slimevoid.gamemodes.bombermine.blocks.BlockFireBomber;
import net.slimevoid.gamemodes.bombermine.blocks.BlockPodium;
import net.slimevoid.gamemodes.bombermine.gui.GuiBomberOverlay;
import net.slimevoid.gamemodes.bombermine.network.CommonPacketHandler;
import net.slimevoid.gamemodes.bombermine.network.ConnectionHandler;
import net.slimevoid.gamemodes.bombermine.proxy.CommonProxy;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBomb;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "bomberMine", name = "BomberMine", version = "0")
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = true,
		clientPacketHandlerSpec = @SidedPacketHandler(
				channels = { "BOMBERMINE" },
				packetHandler = CommonPacketHandler.class),
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
	public Block INVISIBLE;
	public BlockPodium PODUIM;
	public BlockBonus BONUS;
	public Map<String, Integer> scores;
	private String mapName;
	private String mapAuthor;
	private String mapComment;
	private long timeEndingBuildingMap;
	private long timeEndingRound;
	
	@SidedProxy(clientSide = "net.slimevoid.gamemodes.bombermine.proxy.ClientProxy", serverSide = "net.slimevoid.gamemodes.bombermine.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@SideOnly(value = Side.CLIENT)
	public static GuiBomberOverlay overlay = new GuiBomberOverlay();
	
	@MLProp(info = "Amount of time in ticks (50ms) when the map is building (400 ticks = 20secs)", max = 6000, min = 0, name = "PreStartLenght") 	public static int preStartLenght = 400;
	@MLProp(info = "Time limmit per round in ticks (50ms)", max = 120000, min = 0, name = "RoundTimeLimmit") 										public static int roundTimeLimmit = 6000;
	@MLProp(info = "Amount of points give to the killer and removed from the victim", max = 10000, min = 0, name = "PtsForKill") 					public static int ptsForKill = 100;
	@MLProp(info = "Amount of points removed for a suicide and shared between the alive players", max = 10000, min = 0, name = "PtsForSuicide") 	public static int ptsForSuicide = 400;
	@MLProp(info = "Ignored maps, separated by \", \"", max = 0, min = 0, name = "IgnoredMaps") 													public static String ignoredMaps = "";
	@MLProp(info = "How the map is changed (0 = random, 1 = normal (first map then second...))", max = 1, min = 0, name = "MapChangeMode") 			public static int mapChangeMode = 0;
	
	public BomberMineFML() {
		scores = new HashMap<>();
		mapName = "Map Name";
		mapAuthor = "Author";
		mapComment = "Comment";
	}
	
	@Init
	public void load(FMLInitializationEvent e) {
		instance = this;
		
//		GameRegistry.registerWorldGenerator(new IWorldGenerator() {
//			@Override
//			public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
//				Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
//				byte[] ids = new byte[16 * 16 * world.getHeight()];
//				for(int i = 0; i < ids.length; i ++) ids[i] = 0;
//				chunk.fillChunk(ids, 16, 16, true);
//			}
//		});
		
		BOMB = new BlockBomb(151, Block.obsidian.blockIndexInTexture, 0x21ff2b, 1);
		BOMB.setRequiresSelfNotify();
		GameRegistry.registerBlock(BOMB);
		
		BOMB_LASER = new BlockBombLaser(152, Block.obsidian.blockIndexInTexture, 0x6000ff, .25);
		BOMB_LASER.setRequiresSelfNotify();
		GameRegistry.registerBlock(BOMB_LASER);
		
		BOMB_ICE = new BlockBombIce(153, Block.ice.blockIndexInTexture, 0x0000FF, .1);
		BOMB_ICE.setRequiresSelfNotify();
		GameRegistry.registerBlock(BOMB_ICE);
		
		BOMB_FIRE = new BlockBombFire(154, Block.netherrack.blockIndexInTexture, 0xdcab26, .1);
		BOMB_FIRE.setRequiresSelfNotify();
		GameRegistry.registerBlock(BOMB_FIRE);
		
		FIRE = new BlockFireBomber(155, Block.fire.blockIndexInTexture);
		GameRegistry.registerBlock(FIRE);
		
		INVISIBLE = new Block(156, Material.air) {
			@Override
			public boolean isOpaqueCube() {
				return false;
			}
			
			@Override
			public int getRenderType() {
				return -1;
			}
			
			@Override
			public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
				setBlockBounds(0, 0, 0, 1, 1, 1);
				return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
			}
			
			@Override
			public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
				setBlockBounds(0, 0, 0, 0, 0, 0);
			}
		};
		
		PODUIM = new BlockPodium(157);
		PODUIM.setRequiresSelfNotify();
		GameRegistry.registerBlock(PODUIM);
		
		BONUS = new BlockBonus(158);
		GameRegistry.registerBlock(BONUS);
		
		BOMB_COBWEB = new BlockBombCobweb(159, Block.cobblestoneMossy.blockIndexInTexture, 0x49ff20, .75);
		BOMB_COBWEB.setRequiresSelfNotify();
		GameRegistry.registerBlock(BOMB_COBWEB);
		
		GameRegistry.registerTileEntity(TileEntityBomb.class, "Bomb");
		
		proxy.init();
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
