package net.slimevoid.gamemodes.bombermine.tileentities;

import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.TileEntity;

public class TileEntityOwn extends TileEntity {
	
	public void setOwner(EntityPlayerMP poser) {
		this.owner = poser;
	}

	public EntityPlayerMP getOwner() {
		return owner;
	}
	
	private EntityPlayerMP owner = null;
}
