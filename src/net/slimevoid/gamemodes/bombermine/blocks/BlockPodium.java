package net.slimevoid.gamemodes.bombermine.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityPodium;

public class BlockPodium extends BlockContainer {
	public BlockPodium(int id) {
		super(id, Material.glass);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}


	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityPodium();
	}
}
