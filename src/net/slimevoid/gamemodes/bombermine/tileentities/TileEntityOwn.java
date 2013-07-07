package net.slimevoid.gamemodes.bombermine.tileentities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class TileEntityOwn extends TileEntity {
	
	public void setOwner(EntityPlayerMP poser) {
		this.owner = poser;
	}

	public EntityPlayerMP getOwner() {
		return owner;
	}
	
	private EntityPlayerMP owner = null;
}
