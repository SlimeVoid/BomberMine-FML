package net.slimevoid.gamemodes.bombermine.proxy;

import net.minecraft.block.material.Material;
import net.slimevoid.gamemodes.bombermine.BomberMineFML;
import net.slimevoid.gamemodes.bombermine.BombermineMainSession;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBomb;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBombCobweb;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBombFire;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBombIce;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBombLaser;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBonus;
import net.slimevoid.gamemodes.bombermine.blocks.BlockFireBomber;
import net.slimevoid.gamemodes.bombermine.blocks.BlockInvisible;
import net.slimevoid.gamemodes.bombermine.blocks.BlockPodium;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBomb;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBonus;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityPodium;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {
	
	public BombermineMainSession mainSession;
	
	public void init() {
		initCommons();
		
		TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
		GameRegistry.registerTileEntity(TileEntityBomb.class, "Bomb");
		GameRegistry.registerTileEntity(TileEntityBonus.class, "Bonus");
		GameRegistry.registerTileEntity(TileEntityPodium.class, "Podium");
	}
	
	public void initCommons() {
		BlockBomb BOMB = new BlockBomb(1151, 0x21ff2b, 1);
		BOMB.setUnlocalizedName("BOMB");
		LanguageRegistry.addName(BOMB, "BOMB");
		GameRegistry.registerBlock(BOMB, "BOMB");
		BomberMineFML.instance.BOMB = BOMB;

		BlockBombLaser BOMB_LASER = new BlockBombLaser(1152, 0x6000ff, .25);
		BOMB_LASER.setUnlocalizedName("BOMB_LASER");
		LanguageRegistry.addName(BOMB_LASER, "BOMB_LASER");
		GameRegistry.registerBlock(BOMB_LASER, "BOMB_LASER");
		BomberMineFML.instance.BOMB_LASER = BOMB_LASER;
		
		BlockBombIce BOMB_ICE = new BlockBombIce(1153, 0x0000FF, .1);
		BOMB_ICE.setUnlocalizedName("BOMB_ICE");
		LanguageRegistry.addName(BOMB_ICE, "BOMB_ICE");
		GameRegistry.registerBlock(BOMB_ICE, "BOMB_ICE");
		BomberMineFML.instance.BOMB_ICE = BOMB_ICE;
		
		BlockBombFire BOMB_FIRE = new BlockBombFire(1154, 0xdcab26, .1);
		BOMB_FIRE.setUnlocalizedName("BOMB_FIRE");
		LanguageRegistry.addName(BOMB_FIRE, "BOMB_FIRE");
		GameRegistry.registerBlock(BOMB_FIRE, "BOMB_FIRE");
		BomberMineFML.instance.BOMB_FIRE = BOMB_FIRE;
		
		BlockFireBomber FIRE = new BlockFireBomber(1155);
		FIRE.setUnlocalizedName("FIRE");
		LanguageRegistry.addName(FIRE, "FIRE");
		GameRegistry.registerBlock(FIRE, "FIRE");
		BomberMineFML.instance.FIRE = FIRE;
		
		BlockInvisible INVISIBLE = new BlockInvisible(1156, Material.air);
		INVISIBLE.setUnlocalizedName("INVISIBLE");
		LanguageRegistry.addName(INVISIBLE, "INVISIBLE");
		GameRegistry.registerBlock(INVISIBLE, "INVISIBLE");
		BomberMineFML.instance.INVISIBLE = INVISIBLE;
		
		BlockPodium PODUIM = new BlockPodium(1157);
		PODUIM.setUnlocalizedName("PODUIM");
		LanguageRegistry.addName(PODUIM, "PODUIM");
		GameRegistry.registerBlock(PODUIM, "PODUIM");
		BomberMineFML.instance.PODUIM = PODUIM;
		
		BlockBonus BONUS = new BlockBonus(1158);
		BONUS.setUnlocalizedName("BONUS");
		LanguageRegistry.addName(BONUS, "BONUS");
		GameRegistry.registerBlock(BONUS, "BONUS");
		BomberMineFML.instance.BONUS = BONUS;
		
		BlockBombCobweb BOMB_COBWEB = new BlockBombCobweb(1159, 0x49ff20, .75);
		BOMB_COBWEB.setUnlocalizedName("BOMB_COBWEB");
		LanguageRegistry.addName(BOMB_COBWEB, "BOMB_COBWEB");
		GameRegistry.registerBlock(BOMB_COBWEB, "BOMB_COBWEB");
		BomberMineFML.instance.BOMB_COBWEB = BOMB_COBWEB;
		
		mainSession = new BombermineMainSession();
	}
}
