package net.slimevoid.gamemodes.bombermine.proxy;

import net.slimevoid.gamemodes.bombermine.gui.GuiBomberOverlay;
import net.slimevoid.gamemodes.bombermine.render.BombRenderer;
import net.slimevoid.gamemodes.bombermine.render.BonusRenderer;
import net.slimevoid.gamemodes.bombermine.render.PodiumRenderer;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBomb;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBonus;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityPodium;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {
	public static GuiBomberOverlay overlay = new GuiBomberOverlay();
	
	@Override
	public void init() {
		super.initCommons();
		RenderingRegistry.registerBlockHandler(new BombRenderer());
		TickRegistry.registerTickHandler(new CommonTickHandler(), Side.CLIENT);
		GameRegistry.registerTileEntity(TileEntityBomb.class, "Bomb");
		GameRegistry.registerTileEntity(TileEntityBonus.class, "Bonus");
		GameRegistry.registerTileEntity(TileEntityPodium.class, "Podium");
	}
}
