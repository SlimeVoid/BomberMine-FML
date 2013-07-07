package net.slimevoid.gamemodes.bombermine.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;

public class BlockBombLaser extends BlockBomb {

	// CLIENT
	
	public BlockBombLaser(int id, int color, double dropRate) {
		super(id, color, dropRate);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = Block.obsidian.getIcon(0, 0);
    }

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if(world.getBlockMetadata(x, y, z) == 2) {
			for(int i = 1; i < RANGE; i++) {
				if(testAt(world, x-i, y, z, true)) {
					break;
				}
			}
			for(int i = 1; i < RANGE; i++) {
				if(testAt(world, x+i, y, z, true)) {
					break;
				}
			}
			for(int j = 1; j < RANGE; j++) {
				if(testAt(world, x, y, z-j, false)) {
					break;
				}
			}
			for(int j = 1; j < RANGE; j++) {
				if(testAt(world, x, y, z+j, false)) {
					break;
				}
			}
		}
	}
	
	private boolean testAt(World world, int x, int y, int z, boolean dir) {
		if(world.getBlockId(x, y, z) > 0) {
			return true;
		} else {
			for(int i = 0; i < 4; i++) {
				if (world.rand.nextDouble() < 0.125) {
					if (dir) {
						world.spawnParticle("reddust", x + i * 0.25, y + 0.5,
								z + 0.5, 0.9 + world.rand.nextDouble() * 0.2,
								0 + world.rand.nextDouble() * 0.2, 1);
					} else {
						world.spawnParticle("reddust", x + 0.5, y + 0.5, z + i
								* 0.25, 0.9 + world.rand.nextDouble() * 0.2,
								0 + world.rand.nextDouble() * 0.2, 1);
					}
				}
			}
			return false;
		}
	}
	
	private static final int RANGE = 50;
}
