package net.slimevoid.gamemodes.bombermine.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.slimevoid.gamemodes.bombermine.BomberMineFML;

public class BlockBombFire extends BlockBomb {

	public BlockBombFire(int id, int color, double dropRate) {
		super(id, color, dropRate);
		
		rBase = 1; 		rRand = 0.2F;
		gBase = 0F; 	gRand = 0.3F;
		bBase = 0F;		bRand = 0.3F;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = Block.netherrack.getIcon(0, 0);
    }

	@Override
	protected boolean explodeBlockClient(World world, int x, int y, int z, boolean passTroughWall) {
		int id = world.getBlockId(x, y, z);
		
		if(id != 0 && id != BomberMineFML.instance.BOMB_FIRE.blockID) {
			return !passTroughWall || id == 7;
		}
		for(int i = 0; i < 15; i++) {
			world.spawnParticle("reddust", x + world.rand.nextFloat(), y + world.rand.nextFloat(), z + world.rand.nextFloat(), r, g, b);
		}
		return false;
	}
}
