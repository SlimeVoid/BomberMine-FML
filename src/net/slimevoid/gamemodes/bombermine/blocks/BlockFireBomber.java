package net.slimevoid.gamemodes.bombermine.blocks;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockFireBomber extends Block {
	
	public BlockFireBomber(int id, int texture) {
		super(id, texture, Material.fire);
		setBlockBounds(0, 0, 0, 0, 0, 0);
	}

	@Override
	public int getRenderType() {
		return Block.fire.getRenderType();
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
	}
}
