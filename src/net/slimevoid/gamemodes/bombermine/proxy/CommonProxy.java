package net.slimevoid.gamemodes.bombermine.proxy;

import net.slimevoid.gamemodes.bombermine.BombermineMainSession;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBonus;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityPodium;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

public class CommonProxy {
	
	public BombermineMainSession mainSession;
	
	public void init() {
		initCommons();
		GameRegistry.registerTileEntity(TileEntityBonus.class, "Bonus");
		GameRegistry.registerTileEntity(TileEntityPodium.class, "Podium");
	}
	
	public void initCommons() {
		mainSession = new BombermineMainSession();
		TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
	}
}
