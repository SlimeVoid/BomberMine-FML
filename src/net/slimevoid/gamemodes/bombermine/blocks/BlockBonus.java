package net.slimevoid.gamemodes.bombermine.blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBonus;

public class BlockBonus extends BlockContainer {

	public BlockBonus(int id) {
		super(id, Material.circuits);
		this.setBlockBounds(0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(world.isRemote) {
			if(entity instanceof EntityPlayer && world.getBlockMetadata(x, y, z) != 1) {
				Random rand = world.rand;
				world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "random.pop", 0.2F, ((rand .nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				world.setBlockMetadataWithNotify(x, y, z, 1, 2);
			}
		} else {
			if(entity instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) entity;
				
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				if(tileEntity instanceof TileEntityBonus) {
					TileEntityBonus tile = (TileEntityBonus) tileEntity;
					
					if(Item.itemsList[tile.getBonusItemID()] != null) {
						player.inventory.addItemStackToInventory(new ItemStack(tile.getBonusItemID(), 1, 0));
					}
					world.setBlock(x, y, z, 0, 0, 2);
				}
			}
		}
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBonus();
	}
}