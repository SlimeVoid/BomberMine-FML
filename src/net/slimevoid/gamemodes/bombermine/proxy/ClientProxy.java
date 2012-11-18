package net.slimevoid.gamemodes.bombermine.proxy;

import net.slimevoid.gamemodes.bombermine.render.BombRenderer;
import net.slimevoid.gamemodes.bombermine.render.BonusRenderer;
import net.slimevoid.gamemodes.bombermine.render.PoduimRenderer;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBonus;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityPodium;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void init() {
		super.initCommons();
		TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
		RenderingRegistry.registerBlockHandler(new BombRenderer());
		ClientRegistry.registerTileEntity(TileEntityBonus.class, "Bonus", new BonusRenderer());
		ClientRegistry.registerTileEntity(TileEntityPodium.class, "Podium", new PoduimRenderer());
	}
}
