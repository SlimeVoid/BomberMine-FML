package net.slimevoid.gamemodes.bombermine.blocks;

import net.minecraft.src.World;

public class BlockBombIce extends BlockBomb {

	public BlockBombIce(int id, int texture, int color, double dropRate) {
		super(id, texture, color, dropRate);
		
		rBase = 0.2F; 		rRand = 0.1F;
		gBase = 0.5F; 		gRand = 0.1F;
		bBase = 0.9F;		bRand = 0.2F;
	}

	@Override
	protected boolean explodeBlockClient(World world, int x, int y, int z, boolean passTroughWall) {
		int id = world.getBlockId(x, y, z);
		
		if(id != 0) {
			return !passTroughWall || id == 7;
		}
		for(int i = 0; i < 15; i++) {
			world.spawnParticle("reddust", x + world.rand.nextFloat(), y + world.rand.nextFloat(), z + world.rand.nextFloat(), r, g, b);
		}
		return false;
	}
}
