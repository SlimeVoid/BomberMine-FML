package net.slimevoid.gamemodes.bombermine.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

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
