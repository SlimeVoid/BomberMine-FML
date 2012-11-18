package net.slimevoid.gamemodes.bombermine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBomb;
import net.slimevoid.gamemodes.bombermine.bonus.Bonus;
import net.slimevoid.gamemodes.bombermine.map.BombermanMap;
import net.slimevoid.gamemodes.bombermine.map.BombermanMapManager;

public class BombermanGameSession extends GameSession {

	private MinecraftServer server;

	public BombermanGameSession() {
		super("BomberMine session", BomberMineFML.roundTimeLimmit + BomberMineFML.preStartLenght);
		preStart = BomberMineFML.preStartLenght;
		mapManager = new BombermanMapManager();
		nRounds = 0;
		server = MinecraftServer.getServer();
	}

	@Override
	public void onPreStart() {
		gameMap = mapManager.getNextMap();
		
		nRounds ++;
		if(nRounds >= roundsUntilReset && roundsUntilReset != 0) {
			List<String> winners = BomberMineFML.proxy.mainSession.getPlayersWithHighestScore();
			String names = "";
			if(!winners.isEmpty()) {
				if(winners.size() == 1) {
					names = winners.get(0);
				} else {
					for(String winner : winners) {
						String liaison = winners.indexOf(winner) == 0 ? "" : (winners.indexOf(winner) == winners.size()-1 ? " and " : ", ");
						names += liaison + winner;
					}
				}
				Utils.sendChatToAll(roundsUntilReset+" rounds ended. "+names+" won!");
			}
			nRounds = 0;
		}
		
		for(Block block : Block.blocksList) {
			if(block != null) {
				try {
//					ModLoader.setPrivateValue(Block.class, block, 142, new Float(-1F)); TODO Unbreakable blocks
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		world = server.worldServerForDimension(0);
		for (Object entry : server.getConfigurationManager().playerEntityList) {
			EntityPlayerMP player = (EntityPlayerMP)entry;
			setPlayerSpec(player);
			
//			Packet230ModLoader packet = new Packet230ModLoader(); TODO spec packet
//			packet.dataInt = new int[]{1,BomberMineFML.proxy.mainSession.getScore(player)};
//			packet.dataString = new String[]{player.username.toLowerCase()};
//			ModLoaderMp.sendPacketToAll(BomberMineFML.instance, packet);
		}
		
		cleanEntities();
		System.out.println("Starting building map...");
		long startTime = System.currentTimeMillis();
		gameMap.buildMap(server.getConfigurationManager().playerEntityList.size());
		System.out.println("Done in "+(System.currentTimeMillis() - startTime)+" ms");
		playersAtStartOfTheRound = 0;
	}

	@Override
	public void onStart() {
		int spawnOffset = 0;
		
		if(nRounds == 0) {
			BomberMineFML.proxy.mainSession.resetScores();
		}
		
		gameMap.buildSpawns(server.getConfigurationManager().playerEntityList.size());
		
		for (Object entry : server.getConfigurationManager().playerEntityList) {
			EntityPlayerMP player = (EntityPlayerMP)entry;
			
			addPlayerToGame(player, spawnOffset);
			
			spawnOffset ++;
			
//			Packet230ModLoader packet = new Packet230ModLoader(); TODO round started packet
//			packet.dataInt = new int[]{4,BomberMineFML.roundTimeLimmit*50};
//			
//			ModLoaderMp.sendPacketTo(BomberMineFML.instance, player, packet);
		}
		cleanEntities();
		playersAtStartOfTheRound = inGamePlayers.size();
	}
	
	@Override
	public void onTick() {
		super.onTick();
		
		gameMap.buildTick();
		
		for(EntityPlayerMP player : inGamePlayers) {
			if(player.isDead) {
				toBeRemoved.add(player);
			}
		}
		world = server.worldServerForDimension(0);
		for (Object entry : server.getConfigurationManager().playerEntityList) {
			EntityPlayerMP player = (EntityPlayerMP)entry;
			if(player.posY < 69 && !inGamePlayers.contains(player)){
				setPlayerSpec(player);
			} else if(player.posY < 10) {
				try {
					ModLoader.setPrivateValue(Entity.class, player, 34, new Integer(0));
				} catch (Exception e) {
					e.printStackTrace();
				}
				BomberMineFML.proxy.mainSession.registerSuicide(player);
				setPlayerSpec(player);
				Utils.sendChatToAll(player.username+" fell out of the map!");
				player.prevPosY = player.posY;
			} else {
				for(int i = 0; i < player.inventory.mainInventory.length; i++) {
					ItemStack item = player.inventory.mainInventory[i];
					if(item != null) {
						if((item.itemID < 256 && Block.blocksList[item.itemID] != null && Block.blocksList[item.itemID] instanceof BlockBomb)) {
							BlockBomb bomb = (BlockBomb) Block.blocksList[item.itemID];
							if(i != bomb.getPosInInventory()) { 
								if(player.inventory.mainInventory[bomb.getPosInInventory()] != null && player.inventory.mainInventory[bomb.getPosInInventory()].itemID == item.itemID) {
									player.inventory.mainInventory[bomb.getPosInInventory()].stackSize+=item.stackSize;
									if(player.inventory.mainInventory[bomb.getPosInInventory()].stackSize > 64) {
										player.inventory.mainInventory[bomb.getPosInInventory()].stackSize = 64;
									}
								} else {
									player.inventory.mainInventory[bomb.getPosInInventory()] = item;
								}
								player.inventory.mainInventory[i] = null;
							}
						} else {
							Bonus bonus = Bonus.getBonusByPos(i);
							if(bonus == null) {
								Bonus bonusById = Bonus.getBonusById(item.itemID);
								if(bonusById != null) {
									if(player.inventory.mainInventory[bonusById.getPosInInventory()] != null) {
										player.inventory.mainInventory[bonusById.getPosInInventory()].stackSize+=item.stackSize;
									} else {
										player.inventory.mainInventory[bonusById.getPosInInventory()] = item;
									}
								}
								player.inventory.mainInventory[i] = null;
							} else if(item.itemID != bonus.getItem().itemID) {
								player.inventory.mainInventory[i] = new ItemStack(bonus.getItem().itemID, bonus.getStartAmount(), 0);
							} else if(item.stackSize > bonus.getMax()+1) {
								player.inventory.mainInventory[i].stackSize = bonus.getMax()+1;
							}
						}
					}
				}
				if(Bonus.hasPlayerAllBonus(player, new Bonus[]{Bonus.SLOW, Bonus.SPEED})) {
					if(Math.random() > 0.5){
						player.inventory.mainInventory[Bonus.SPEED.getPosInInventory()].stackSize = 1;
					} else {
						player.inventory.mainInventory[Bonus.SLOW.getPosInInventory()].stackSize = 1;
					}
				}
				float speed = 1F;
//				if(BomberMineFML.getPlayerIsInWeb(player)) { TODO is in web
//					speed*=1.5;
//				}
				if(Bonus.hasPlayerBonus(player, Bonus.SPEED)) {
					speed*=1.2;
				}
				if(Bonus.hasPlayerBonus(player, Bonus.SLOW)) {
					speed*=0.8;
				}
				if(speed != 1F && player.onGround) {
					player.motionX*=speed;
					player.motionY*=speed;
					player.motionZ*=speed;
				}
				if(player.isSprinting()) {
					player.setSprinting(false);
				}
			}
		}
		
		for(EntityPlayerMP player : toBeRemoved) {
			inGamePlayers.remove(player);
			BomberMineFML.proxy.mainSession.registerSuicide(player);
		}
		toBeRemoved.clear();
		
		if(isStarted && (inGamePlayers.size() < 1 || (inGamePlayers.size() < 2 && server.getConfigurationManager().playerEntityList.size() > 1))) {
			restart();
		}
	}

	@Override
	public boolean canStart() {
		return server.getConfigurationManager().playerEntityList.size() > 0 && !isStarted;
	}

	@Override
	public void onStop() {
	}

	@Override
	public boolean autoRestart() {
		return true;
	}	
	
	private void addPlayerToGame(EntityPlayerMP player, int spawnOffset) {
		if(!inGamePlayers.contains(player)) {
			inGamePlayers.add(player);
		}
		BombermanMap.Spawn spawn = gameMap.spawnList.get(world.rand.nextInt(gameMap.spawnList.size()));
		player.playerNetServerHandler.setPlayerLocation(spawn.getX() + 0.5, 66,  spawn.getZ() + 0.5, player.cameraPitch, player.cameraYaw); // TODO is working ?
		gameMap.spawnList.remove(spawn);
		for(int i = 0; i < player.inventory.mainInventory.length; i++) {
			player.inventory.mainInventory[i] = null;
		}
		for(Bonus bonus : Bonus.values()) {
			player.inventory.mainInventory[bonus.getPosInInventory()] = new ItemStack(bonus.getItem().itemID, bonus.getStartAmount(), 0);
		}
		player.inventory.mainInventory[0] = new ItemStack(BomberMineFML.instance.BOMB);
		
//		Packet230ModLoader packet = new Packet230ModLoader(); TODO packet ?
//		packet.dataInt = new int[]{2, 2};
//		packet.dataFloat = new float[]{(float) player.posX, (float) player.posY, (float) player.posZ};
//		packet.dataString = new String[]{player.username};
//		
//		ModLoaderMp.sendPacketToAll(BomberMineFML.instance, packet);
	}
	
	public void setPlayerSpec(EntityPlayerMP player) {
		if(inGamePlayers.contains(player)) {
			inGamePlayers.remove(player);
		}
		
		for(int i = 0; i < player.inventory.mainInventory.length; i++) {
			if(player.inventory.mainInventory[i] != null) {
				player.inventory.mainInventory[i].stackSize--;
				if(player.inventory.mainInventory[i].stackSize == 0) {
					player.inventory.mainInventory[i] = null;
					continue;
				}
				EntityItem drop = new EntityItem(world, player.posX, player.posY+0.5, player.posZ, player.inventory.mainInventory[i]);
				world.spawnEntityInWorld(drop);
				
				player.inventory.mainInventory[i] = null;
			}
		}
		
		player.playerNetServerHandler.setPlayerLocation(mapSize/2D, 73, mapSize/2D, 0, 0);
//		
//		Packet230ModLoader packet = new Packet230ModLoader(); TODO packet spec ?
//		packet.dataInt = new int[]{2, 1};
//		packet.dataFloat = new float[]{(float) player.posX, (float) player.posY, (float) player.posZ};
//		packet.dataString = new String[]{player.username};
//		
//		ModLoaderMp.sendPacketToAll(BomberMineFML.instance, packet);
	}
	
	@Override
	public void restart() {
		super.restart();
		BombermanMap.stopBuilding();
	}

	private void cleanEntities() {
		for(Object obj : world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(-200, 0, -200, 200, 128, 200))) {
			if(obj instanceof Entity) {
				Entity entity = (Entity) obj;
				
				if(!(entity instanceof EntityPlayerMP)) {
					entity.setDead();
					entity.attackEntityFrom(DamageSource.fall, 9999);
				}
			}
		}
	}

	public List<EntityPlayerMP> getInGamePlayers() {
		return inGamePlayers;
	}

	public int getPlayersAtStartOfTheRound() {
		return playersAtStartOfTheRound;
	}
	
	public int mapSize = 31;
	public World world;
	public BombermanMap gameMap;
	
	private int playersAtStartOfTheRound;
	private int nRounds;
	private final BombermanMapManager mapManager;
	
	private final List<EntityPlayerMP> toBeRemoved = new ArrayList<EntityPlayerMP>();
	private final List<EntityPlayerMP> inGamePlayers = new ArrayList<EntityPlayerMP>();
	
	public static int roundsUntilReset;
}
