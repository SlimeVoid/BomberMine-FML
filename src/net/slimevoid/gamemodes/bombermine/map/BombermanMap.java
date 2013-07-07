package net.slimevoid.gamemodes.bombermine.map;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.slimevoid.gamemodes.bombermine.BomberMineFML;

public class BombermanMap {
	
	protected BombermanMap(String name, String author, String comment, List<MapGeneratorFunc> gens) {
		this.name = name;
		this.author = author;
		this.comment = comment;
		this.gens = gens;
		this.world = MinecraftServer.getServer().worldServerForDimension(0);
	}
	 
	@Override
	public String toString() {
		return this.getName();
	}

	public void buildMap(final int size) {
		resetScheduler();
		
//		Packet230ModLoader packet = new Packet230ModLoader(); TODO packet ?
//		packet.dataInt = new int[] {3, 1, mod_BomberMan.getMainSession().gameSession.preStart*50};
//		packet.dataString = new String[] {name, author, comment};
//		ModLoaderMp.sendPacketToAll(mod_BomberMan.instance, packet);
		
		nbPlayers = size;
		mapSize = nbPlayers > 8 ? 31 + (nbPlayers - 8)*8 : 31;
		
		System.out.println("mapSize: "+mapSize);
		
		genBaseMap();
		
		for(MapGeneratorFunc gen : gens) {
			curentScheduleList = new ArrayList<BlockToSchedule>();
			gen.generate();
			toSchedule.add(curentScheduleList);
		}
		curentScheduleList = new ArrayList<BlockToSchedule>();
		genSpawn();
		toSchedule.add(curentScheduleList);
		
		scheduleBlocks();
	}
	
	public void buildSpawns(final int size) {
		nbPlayers = size;
		
		resetScheduler();
		
		curentScheduleList = new ArrayList<BlockToSchedule>();
		genSpawn(); 
		toSchedule.add(curentScheduleList);
		
		scheduleBlocks();
	}
	
	private void scheduleBlocks() {
		int blockSum = 0;
		for(List<BlockToSchedule> list : toSchedule) {
			blockSum+=list.size();
			Collections.sort(list, new Comparator<BlockToSchedule>() {
				@Override
				public int compare(BlockToSchedule block1, BlockToSchedule block2) {
					int diffDist = (int) (Math.round(Math.sqrt(Math.pow(block2.getX() - mapSize/2, 2) + Math.pow(block2.getZ() - mapSize/2, 2))) - Math.round(Math.sqrt(Math.pow(block1.getX() - mapSize/2, 2) + Math.pow(block1.getZ() - mapSize/2, 2))));
					return diffDist;
				}
			});
		}
		int timePerBlock = Math.round(((BomberMineFML.proxy.mainSession.gameSession.preStart - BomberMineFML.proxy.mainSession.gameSession.currentTick)*50-1000) / (float)blockSum);
		
		for(List<BlockToSchedule> list : toSchedule) {
			for(BlockToSchedule block : list) {
				scheduleBlock(block.getX(), block.getZ(), block.getBlock(), timePerBlock);
			}
		}
	}
	
	public void resetScheduler() {
		lastSchedule = System.currentTimeMillis();
		curentScheduleList = null;
		toSchedule.clear();
	}
	
	public static void stopBuilding() {
		for(TimerTask task : tasks) {
			task.cancel();
		}
		tasks.clear();
		System.out.println("Purging "+builder.purge()+" tasks");
	}
	
	public boolean isInMap(int x, int z) {
		return (x >= 1 && x <= mapSize - 1 && z >= 1 && z <= mapSize - 1);
	}
	
	protected void setBlock(final int x, final int z, final int block) {
		curentScheduleList.add(new BlockToSchedule(x, z, block));
	}

	protected void scheduleBlock(final int x, final int z, final int block, long timeToAdd) {
		if (isInMap(x, z)) {
			if(lastSchedule == -1) {
				lastSchedule = System.currentTimeMillis();
			}
			lastSchedule += timeToAdd;
			
			if(lastSchedule < System.currentTimeMillis()) {
				world.setBlock(x, 65, z, block, 0, 2);
				world.setBlock(x, 66, z, block, 0, 2);
			} else {
				TimerTask task = new TimerTask() {
					
					@Override
					public void run() {
						toBuild.add(new BlockToSchedule(x, z, block));
					}
				};
				builder.schedule(task, new Date(lastSchedule));
				tasks.add(task);
			}
		}
	}
	
	public void buildTick() {
		toBuild.build(world);
	}
	
	public void genSpawn() {

		spawnList.clear();
		
		if (nbPlayers > 0)
			spawnList.add(new Spawn(0, 2, 2));
		if (nbPlayers > 1)
			spawnList.add(new Spawn(1, mapSize - 3, 2));
		if (nbPlayers > 2)
			spawnList.add(new Spawn(2, mapSize - 3, mapSize - 3));
		if (nbPlayers > 3)
			spawnList.add(new Spawn(3, 2, mapSize - 3));
		if (nbPlayers > 4)
			spawnList.add(new Spawn(4, mapSize / 2, 2));
		if (nbPlayers > 5)
			spawnList.add(new Spawn(5, mapSize - 3, mapSize / 2));
		if (nbPlayers > 6)
			spawnList.add(new Spawn(6, mapSize / 2, mapSize - 3));
		if (nbPlayers > 7)
			spawnList.add(new Spawn(7, 2, mapSize / 2));
		if (nbPlayers > 8)
			for (int iSpawn = 0; iSpawn < nbPlayers - 8; iSpawn ++) {
				spawnList.add(new Spawn(8 + iSpawn, world.rand.nextInt(mapSize - 6) + 3, world.rand.nextInt(mapSize - 6) + 3));
			}
		
		for (Spawn spawn:spawnList){
			for (int xSpawn = -1; xSpawn < 3; xSpawn ++){
				for (int zSpawn = -1; zSpawn < 3; zSpawn ++) {
					if (xSpawn == -1 || xSpawn == 2 || zSpawn == -1 || zSpawn == 2){
						setBlock(xSpawn + spawn.getX(), zSpawn + spawn.getZ(), 20);
					} else {
						setBlock(xSpawn + spawn.getX(), zSpawn + spawn.getZ(), 0);
					}
				}
			}
		}
	}

	public void genBaseMap() {
		int podiumSizeX = 4;
		int podiumStartX = -3, podiumEndX = podiumStartX + podiumSizeX;
		int podiumY = 67;
		int podiumSizeZ = 10;
		int podiumStartZ = (mapSize/2)-(podiumSizeZ/2) , podiumEndZ = podiumStartZ + podiumSizeZ;
		
		for(int x = -20; x < 60; x++) { // TODO Optimiser
			for(int y = 60; y < 80; y++) {
				for(int z = -20; z < 60; z++) {
					if(y == 64 && x >= 0 && z >= 0 && x <= mapSize && z <= mapSize) {
						continue;
					} else if(y == podiumY && x > podiumStartX && x < podiumEndX && z > podiumStartZ && z < podiumEndZ) {
						continue;
					}
					world.setBlock(x, y, z, 0, 0, 2);
				}
			}
		}
		
		for(int x = podiumStartX; x < podiumEndX; x++) {
			for(int z = podiumStartZ; z < podiumEndZ; z++) {
				world.setBlock(x, podiumY, z, Block.stoneBrick.blockID, 0, 2);
			}
		}
		
		for(int i = 0; i < 3; i++) {
			for(int x = 0; x < 2; x++) {
				for(int y = 0; y < 2+i; y++) {
					for(int z = 0; z < 2; z++) {
						int zPos = z + podiumStartZ + 1 + (i == 0 ? 2 : i == 1 ? 0 : 1)*3;
						if(y < 1+i) {
							world.setBlock(x + 1 + podiumStartX, podiumY+1+y, zPos, Block.obsidian.blockID, 0, 2);
						} else {
							int blockID = i == 0 ? Block.blockIron.blockID : i == 1 ? Block.blockGold.blockID : Block.blockDiamond.blockID;
							world.setBlock(x + 1 + podiumStartX, podiumY+1+y, zPos, blockID, 0, 2);
							if(x == 0 && z == 0) {
								world.setBlock(x+ 1 + podiumStartX, podiumY+2+y, zPos, BomberMineFML.instance.PODUIM.blockID, 2-i, 2);
							}
						}
					}
				}
			}
		}
		
		for(int x = 0; x < mapSize +1; x ++){
			for(int z = 0; z < mapSize +1; z ++){
				if (x == 0 || x == mapSize || z == 0 || z == mapSize) {
					world.setBlock(x, 65, z, 7, 0, 2);
					world.setBlock(x, 66, z, 7, 0, 2);
					
					world.setBlock(x, 71, z, BomberMineFML.instance.INVISIBLE.blockID, 0, 2);
					world.setBlock(x, 72, z, BomberMineFML.instance.INVISIBLE.blockID, 0, 2);
				}
				world.setBlock(x, 64, z, 7, 0, 2);
				world.setBlock(x, 70, z, BomberMineFML.instance.INVISIBLE.blockID, 0, 2);
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getComment() {
		return comment;
	}
	
	public List<MapGeneratorFunc> getGens() {
		return gens;
	}

	private final String name, author, comment;
	
	public World world;
	public int mapSize;
	public int nbPlayers;
	
	private List<BlockToSchedule> curentScheduleList;
	private List<List<BlockToSchedule>> toSchedule = new ArrayList<List<BlockToSchedule>>();
	
	private static final Timer builder = new Timer();
	private static final List<TimerTask> tasks = new ArrayList<TimerTask>();
	private static long lastSchedule = -1;

	public static String ignoredMaps = "";

	public static int mapChangeMode;
	private static final ToBuildList toBuild = new ToBuildList();
	
	public List<Spawn> spawnList = new ArrayList<BombermanMap.Spawn>();
	private final List<MapGeneratorFunc> gens;
	@SuppressWarnings("unused")
	private final static Map<String, BombermanMap> maps = new HashMap<String, BombermanMap>();
//	protected final static MinecraftServer server = ModLoader.getMinecraftServerInstance();
	
	public class Spawn {
		public Spawn(int id, int x, int z) {
			this.setSpawnId(id);
			this.setX(x);
			this.setZ(z);
		}
		
		public void setX(int x) {
			this.x = x;
		}

		public int getX() {
			return x;
		}

		public void setZ(int z) {
			this.z = z;
		}

		public int getZ() {
			return z;
		}

		public void setSpawnId(int spawnId) {
			this.spawnId = spawnId;
		}

		public int getSpawnId() {
			return spawnId;
		}

		private int spawnId, x, z;
	}
	
	private class BlockToSchedule {
		public BlockToSchedule(int x, int z, int block) {
			this.x = x;
			this.z = z;
			this.block = block;
		}
		
		public int getX() {
			return x;
		}

		public int getZ() {
			return z;
		}

		public int getBlock() {
			return block;
		}

		private final int x, z, block;
	}
	
	private static class ToBuildList extends ArrayList<BlockToSchedule> {
		private static final long serialVersionUID = 7837584248174001434L;
		@Override
		public synchronized boolean add(BlockToSchedule e) {
			return super.add(e);
		}
		
		public synchronized void build(World world) {
			if(!isEmpty()) {
				for(BlockToSchedule block : this) {
					world.setBlock(block.x, 65, block.z, block.block, 0, 2);
					world.setBlock(block.x, 66, block.z, block.block, 0, 2);
				}
			}
			this.clear();
		}
	}
}
