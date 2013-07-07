package net.slimevoid.gamemodes.bombermine.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockFireBomber extends Block {
	
	public BlockFireBomber(int id) {
		super(id, Material.fire);
		setBlockBounds(0, 0, 0, 0, 0, 0);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = Block.fire.getIcon(0, 0);
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
