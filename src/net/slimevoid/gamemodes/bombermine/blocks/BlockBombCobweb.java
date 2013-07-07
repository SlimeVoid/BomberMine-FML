package net.slimevoid.gamemodes.bombermine.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.slimevoid.gamemodes.bombermine.BomberMineFML;

public class BlockBombCobweb extends BlockBomb {

	public BlockBombCobweb(int id, int color, double dropRate) {
		super(id, color, dropRate);
		
		rBase = 0.6F; 		rRand = 0.1F;
		gBase = 0.7F; 		gRand = 0.4F;
		bBase = 0.5F;		bRand = 0.1F;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = Block.cobblestoneMossy.getIcon(0, 0);
    }

	@Override
	protected boolean explodeBlockClient(World world, int x, int y, int z, boolean passTroughWall) {
		int id = world.getBlockId(x, y, z);
		
		if(id != 0 && id != BomberMineFML.instance.BOMB_COBWEB.blockID) {
			return !passTroughWall || id == 7;
		}
		for(int i = 0; i < 15; i++) {
			world.spawnParticle("reddust", x + world.rand.nextFloat(), y + world.rand.nextFloat(), z + world.rand.nextFloat(), r, g, b);
		}
		return false;
	}
}
