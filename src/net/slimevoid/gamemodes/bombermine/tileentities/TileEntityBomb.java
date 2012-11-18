package net.slimevoid.gamemodes.bombermine.tileentities;

import net.minecraft.src.Block;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBomb;

public class TileEntityBomb extends TileEntityOwn {
	// SERVER
	
	@Override
	public void updateEntity() {
		int blockId = worldObj.getBlockId(xCoord, yCoord, zCoord);
		
		if(Block.blocksList[blockId] instanceof BlockBomb) {
			((BlockBomb)Block.blocksList[blockId]).onTileEntityTick(worldObj, xCoord, yCoord, zCoord, this);
		}
	}
}