package net.slimevoid.gamemodes.bombermine.blocks;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
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
