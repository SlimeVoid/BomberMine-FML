package net.slimevoid.gamemodes.bombermine.tileentities;

import net.minecraft.src.INetworkManager;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;

public class TileEntityBonus extends TileEntity {
	
	public void setBonusItemID(int bonusItemID) {
		this.bonusItemID = bonusItemID;
	}

	public int getBonusItemID() {
		return bonusItemID;
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("bonusItemID", bonusItemID);
		Packet132TileEntityData packet = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
		return packet;
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		super.onDataPacket(net, pkt);
		bonusItemID = pkt.customParam1.getInteger("bonusItemID");
	}

	private int bonusItemID = 0;
}
