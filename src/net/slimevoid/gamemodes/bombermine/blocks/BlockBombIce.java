package net.slimevoid.gamemodes.bombermine.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;

public class BlockBombIce extends BlockBomb {

	public BlockBombIce(int id, int color, double dropRate) {
		super(id, color, dropRate);
		
		rBase = 0.2F; 		rRand = 0.1F;
		gBase = 0.5F; 		gRand = 0.1F;
		bBase = 0.9F;		bRand = 0.2F;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = Block.ice.getIcon(0, 0);
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
