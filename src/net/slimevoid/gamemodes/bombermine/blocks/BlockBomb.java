package net.slimevoid.gamemodes.bombermine.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.slimevoid.gamemodes.bombermine.BomberMineFML;
import net.slimevoid.gamemodes.bombermine.Utils;
import net.slimevoid.gamemodes.bombermine.bonus.Bonus;
import net.slimevoid.gamemodes.bombermine.bonus.Drop;
import net.slimevoid.gamemodes.bombermine.network.CommonPacketHandler;
import net.slimevoid.gamemodes.bombermine.render.BombRenderer;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBomb;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBonus;

public class BlockBomb extends BlockContainer {
	
	protected List<ChunkCoordinates> toDeleteAndDrop = new ArrayList<ChunkCoordinates>();
	protected List<EntityPlayerMP> killActors = new ArrayList<EntityPlayerMP>();
	
	private final int posInInventory;
	private static int lastPosInInventory = -1;
	private static final double DROP_RATE = 0.25;
	
	protected float r, g, b;
	protected float rBase, gBase, bBase, rRand, gRand, bRand;
	
	public BlockBomb(int id, int texture, int color, double dropRate) {
		super(id, texture, Material.iron);
		rBase = 1; 		rRand = 0.2F;
		gBase = 0.6F; 	gRand = 0.2F;
		bBase = 0;		bRand = 0;
		
		lastPosInInventory++;
		posInInventory = lastPosInInventory;
		
		new Drop(id, dropRate, color);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+2, z+1);
	}
	
	public void explodeClient(World world, int x, int y, int z, int rayon, boolean passTroughWall) {
		Minecraft.getMinecraft().sndManager.playSound("random.explode", x + .5F, y + .5F, z + .5F, 4, (world.rand.nextFloat() - world.rand.nextFloat()) * .2F + .7F);
		world.setBlockWithNotify(x, y, z, 0);
		for(int i = 0; i < rayon; i++) {
			if(explodeBlockClient(world, x-i, y, z, passTroughWall)) {
				break;
			}
		}
		for(int i = 0; i < rayon; i++) {
			if(explodeBlockClient(world, x+i, y, z, passTroughWall)) {
				break;
			}
		}
		for(int j = 0; j < rayon; j++) {
			if(explodeBlockClient(world, x, y, z-j, passTroughWall)) {
				break;
			}
		}
		for(int j = 0; j < rayon; j++) {
			if(explodeBlockClient(world, x, y, z+j, passTroughWall)) {
				break;
			}
		}
	}
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		int metadata = world.getBlockMetadata(x, y, z);
		
		randomizeColors(world.rand);
		for(int i = 0; i < world.rand.nextInt(3)+2; i++) {
			switch(metadata) {
			case 0:
				Minecraft.getMinecraft().sndManager.playSound("random.fuse", x+.5F, y+.5F, z+.5F, 0.05F, 1F);
				world.spawnParticle("reddust", x+0.59375, y+1.1, z+0.53125, r, g, b);
				break;
				
			case 1:
				world.spawnParticle("reddust", x+0.53125, y+1, z+0.53125, r, g, b);
				break;
				
			case 2:
				world.spawnParticle("reddust", x+0.46875, y+0.9375, z+0.53125, r, g, b);
				break;
				
			case 3:
				world.spawnParticle("reddust", x+0.46875, y+0.875, z+0.53125, r, g, b);
				break;
				
			case 4:
				world.spawnParticle("reddust", x+0.46875, y+0.8125, z+0.53125, r, g, b);
				break;
			}
		}
	}
	
	protected boolean explodeBlockClient(World world, int x, int y, int z) {
		return explodeBlockClient(world, x, y, z, false);
	}
	
	protected boolean explodeBlockClient(World world, int x, int y, int z, boolean passTroughWall) {
		int id = world.getBlockId(x, y, z);
		if(world.isRemote) {
			for(int i = 0; i < 15; i++) {
				randomizeColors(world.rand);
				world.spawnParticle("reddust", x + world.rand.nextFloat(), y + world.rand.nextFloat(), z + world.rand.nextFloat(), r, g, b);
			}
			if(id != 0) {
				if(id == BomberMineFML.instance.BONUS.blockID) {
					return false;
				}
				Block block = Block.blocksList[id];
				Minecraft.getMinecraft().sndManager.playSound(block.stepSound.getStepSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				for(int i = 0; i < 10; i++) {
					world.spawnParticle("tilecrack_"+id, x + world.rand.nextFloat(), y + world.rand.nextFloat()*2F, z + world.rand.nextFloat(), 0, 1F, 0);
				}
				return !passTroughWall || id == 7;
			}
			return false;
		} else {
			for(Object obj : world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x+0.1, y, z+0.1, x+0.9, y+2, z+0.9))) {
				if(obj instanceof Entity) {
					Entity entity = (Entity) obj;
					
					if(entity instanceof EntityPlayerMP) {
						EntityPlayerMP player = (EntityPlayerMP) entity;
						
						if(killActors.size() == 1 && killActors.contains(player)) {
							BomberMineFML.proxy.mainSession.registerSuicide(player);
							
							Utils.sendChatToAll(player.username+" suicided!");
						} else {
							BomberMineFML.proxy.mainSession.registerKill(player, killActors);
							
							String actors = "";
							
							boolean firstActor = true;
							for(EntityPlayerMP actor : killActors) {
								if(!actor.equals(player)) {
									String liaison = actor.equals(killActors.get(killActors.size()-1)) ? " and " : ", ";
									actors += ((firstActor) ?" " : liaison)+actor.username;
								}
							}
							Utils.sendChatToAll(player.username+" killed by"+actors+"!");
						}
						
						BomberMineFML.proxy.mainSession.gameSession.setPlayerSpec(player);
					} else {
						entity.attackEntityFrom(DamageSource.explosion, 200);
					}
				}
			}
			
			if(Block.blocksList[id] != null && Block.blocksList[id] instanceof BlockBomb) {
				TileEntityBomb tile = (TileEntityBomb) world.getBlockTileEntity(x, y, z);
				explode(world, x, y, z, getPowerForPlayer(tile.getOwner()));
				return true;
			} else if(id != 0) {
				if(id == BomberMineFML.instance.BONUS.blockID) {
					world.setBlockWithNotify(x, y, z, 0);
					return false;
				} else if(id != 7) {
					ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
					if(!toDeleteAndDrop.contains(coords)) {
						toDeleteAndDrop.add(coords);
					}
					return !passTroughWall;
				} else {
					return true;
				}
			}
			return false;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {
		if(!world.isRemote) {
			TileEntityBomb tile = (TileEntityBomb) world.getBlockTileEntity(x, y, z);
			tile.setOwner((EntityPlayerMP) entityliving);
			world.scheduleBlockUpdate(x, y, z, blockID, 15);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return j == 65;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		int metadata = world.getBlockMetadata(i, j, k);
		TileEntityBomb tile = (TileEntityBomb) world.getBlockTileEntity(i, j , k);
		
		if(metadata < 4) {
			world.setBlockMetadataWithNotify(i, j, k, metadata+1);
			world.scheduleBlockUpdate(i, j, k, blockID, 15);
		} else {
			explode(world, i, j, k, getPowerForPlayer(tile.getOwner()), true);
		}
	}
	
	public void onTileEntityTick(World world, int x, int y, int z, TileEntityBomb tile) {
		
	}
	
	protected void explode(World world, int x, int y, int z, int rayon) {
		explode(world, x, y, z, rayon, false);
	}
	
	protected void explode(World world, int x, int y, int z, int rayon, boolean isFirst) {
		if(isFirst) {
			toDeleteAndDrop.clear();
		}
		
		TileEntityBomb tile = (TileEntityBomb) world.getBlockTileEntity(x, y, z);
		if(this.getClass() == BlockBomb.class) {
			if(tile.getOwner() != null && tile.getOwner().posY < 70) {
				tile.getOwner().inventory.addItemStackToInventory(new ItemStack(BomberMineFML.instance.BOMB));
			}
		}
		
		EntityPlayerMP killer = tile.getOwner();
		
		boolean added = false;
		if(!killActors.contains(killer)) {
			killActors.add(killer);
			added = true;
		}
		
		boolean passTroughWall = Bonus.hasPlayerBonus(killer, Bonus.TROUGH);
		
		Utils.sendPacketToAll(CommonPacketHandler.computePacketBombExplosion(this.blockID, x, y, z, rayon, passTroughWall));
		
		world.setBlockWithNotify(x, y, z, 0);
		
		for(int i = 0; i < rayon; i++) {
			if(explodeBlock(world, x-i, y, z, killer, passTroughWall)) {
				break;
			}
		}
		for(int i = 0; i < rayon; i++) {
			if(explodeBlock(world, x+i, y, z, killer, passTroughWall)) {
				break;
			}
		}
		for(int j = 0; j < rayon; j++) {
			if(explodeBlock(world, x, y, z-j, killer, passTroughWall)) {
				break;
			}
		}
		for(int j = 0; j < rayon; j++) {
			if(explodeBlock(world, x, y, z+j, killer, passTroughWall)) {
				break;
			}
		}
		
		if(isFirst) {
			for(ChunkCoordinates coords : toDeleteAndDrop) {
				int id = world.getBlockId(coords.posX, coords.posY, coords.posZ);
				boolean drop = id != Block.web.blockID && id != BomberMineFML.instance.FIRE.blockID;
				world.setBlockWithNotify(coords.posX, coords.posY, coords.posZ, 0);
				world.setBlockWithNotify(coords.posX, coords.posY+1, coords.posZ, 0);
				if(drop) {
					dropBonus(world, coords.posX, coords.posY, coords.posZ);
				}
			}
			toDeleteAndDrop.clear();
		}
		if(added) {
			killActors.remove(killer);
		}
	}
	protected boolean explodeBlock(World world, int x, int y, int z, EntityPlayerMP killer) {
		return explodeBlock(world, x, y, z, killer, false);
	}
	
	protected boolean explodeBlock(World world, int x, int y, int z, EntityPlayerMP killer, boolean passTroughWall) {
		int id = world.getBlockId(x, y, z);
		
		for(Object obj : world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x+0.1, y, z+0.1, x+0.9, y+2, z+0.9))) {
			if(obj instanceof Entity) {
				Entity entity = (Entity) obj;
				
				if(entity instanceof EntityPlayerMP) {
					EntityPlayerMP player = (EntityPlayerMP) entity;
					
					if(killActors.size() == 1 && killActors.contains(player)) {
						BomberMineFML.proxy.mainSession.registerSuicide(player);
						
						Utils.sendChatToAll(player.username+" suicided!");
					} else {
						BomberMineFML.proxy.mainSession.registerKill(player, killActors);
						
						String actors = "";
						
						boolean firstActor = true;
						for(EntityPlayerMP actor : killActors) {
							if(!actor.equals(player)) {
								String liaison = actor.equals(killActors.get(killActors.size()-1)) ? " and " : ", ";
								actors += ((firstActor) ?" " : liaison)+actor.username;
							}
						}
						Utils.sendChatToAll(player.username+" killed by"+actors+"!");
					}
					
					BomberMineFML.proxy.mainSession.gameSession.setPlayerSpec(player);
				} else {
					entity.attackEntityFrom(DamageSource.explosion, 200);
				}
			}
		}
		
		if(Block.blocksList[id] != null && Block.blocksList[id] instanceof BlockBomb) {
			TileEntityBomb tile = (TileEntityBomb) world.getBlockTileEntity(x, y, z);
			explode(world, x, y, z, getPowerForPlayer(tile.getOwner()));
			return true;
		} else if(id != 0) {
			if(id == BomberMineFML.instance.BONUS.blockID) {
				world.setBlockWithNotify(x, y, z, 0);
				return false;
			} else if(id != 7) {
				ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
				if(!toDeleteAndDrop.contains(coords)) {
					toDeleteAndDrop.add(coords);
				}
				return !passTroughWall;
			} else {
				return true;
			}
		}
		return false;
	}
	
	private void dropBonus(World world, int x, int y, int z) {
		ItemStack itemDropped = null;
		if(world.rand.nextDouble() < DROP_RATE) {
			while(itemDropped == null) {
				int random = world.rand.nextInt(Drop.getDrops().size());
				Drop drop = Drop.getDrops().get(random);
				if(world.rand.nextDouble() < drop.getRate() && Item.itemsList[drop.getItemID()] != null) {
					itemDropped = new ItemStack(drop.getItemID(), 1, 0);
				}
			}
			if(itemDropped != null) {
				world.setBlockWithNotify(x, y, z, BomberMineFML.instance.BONUS.blockID);
				TileEntityBonus tile = (TileEntityBonus) world.getBlockTileEntity(x, y, z);
				if(tile != null) {
					tile.setBonusItemID(itemDropped.itemID);
				} else {
					world.setBlockWithNotify(x, y, z, 0);
					try {
						throw new Exception("Failed to read TileEntityBonus at "+x+", "+y+", "+z);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	protected int getPowerForPlayer(EntityPlayerMP player) {
		return Bonus.getPlayerBonusAmount(player, Bonus.POWER)+1;
	}
	
	protected void randomizeColors(Random random) {
		r = rBase + random.nextFloat() * rRand;
		g = gBase + random.nextFloat() * gRand;
		b = bBase + random.nextFloat() * bRand;
	}
	
	@Override
	public int getRenderType() {
		return BombRenderer.renderId;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public int getPosInInventory() {
		return posInInventory;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBomb();
	}
}
