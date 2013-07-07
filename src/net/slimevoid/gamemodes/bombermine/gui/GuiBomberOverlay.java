package net.slimevoid.gamemodes.bombermine.gui;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.ForgeHooksClient;
import net.slimevoid.gamemodes.bombermine.BomberMineFML;
import net.slimevoid.gamemodes.bombermine.bonus.Bonus;

import org.lwjgl.opengl.GL11;

public class GuiBomberOverlay extends GuiScreen {
	
	public GuiBomberOverlay() {
		mc = ModLoader.getMinecraftInstance();
	}
	
	@Override
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        width = scaledresolution.getScaledWidth();
        height = scaledresolution.getScaledHeight();
		
		renderAllBonus();
		
		if((mc.thePlayer instanceof EntityClientPlayerMP) && mc.gameSettings.keyBindPlayerList.pressed) {
			renderPlayerScores(i, j, f); 
			renderMapInfo(true);
        } else {
        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        	renderMapInfo(false);
        }
	}
	
	private void renderMapInfo(boolean tab) {
		BomberMineFML bomberMine = BomberMineFML.instance;
		
		String name = bomberMine.getMapName(), author = "By "+bomberMine.getMapAuthor(), comment = bomberMine.getMapComment();
		
		FontRenderer fr = mc.fontRenderer;
		
		int commentWidth = width-(width/2+90)-4;
		
		// MAP BUILDING
		if(bomberMine.isBuildingMap()) {
			String buildMapMessage1 = "Building map...";
			String buildMapMessage2 = (bomberMine.getTimeUntilMapBuild()/1000)+" secs left";
			
			int yShift = tab ? 110 : 2;
			
			int buildMapW1 = fr.getStringWidth(buildMapMessage1) + 4;
			int buildMapW2 = fr.getStringWidth(buildMapMessage2) + 4;
			int buildMapW = Math.max(buildMapW1, buildMapW2);
			Gui.drawRect(width/2-buildMapW/2, yShift, width/2+buildMapW/2, yShift + 28, 0x50000000);
			fr.drawStringWithShadow(buildMapMessage1, width/2-buildMapW1/2+2, yShift + 2, 0xfda129);
			fr.drawStringWithShadow(buildMapMessage2, width/2-buildMapW2/2+2, yShift + 13, 0xffdbad);
		}
		
		if(bomberMine.isRoundInProgress()) { // ROUND LIMIT
			String buildMapMessage1 = "Round in progress...";
			String buildMapMessage2 = (bomberMine.getTimeUntilRoundEnded()/1000)+" secs left";
			
			int yShift = tab ? 110 : 2;
			
			int buildMapW1 = fr.getStringWidth(buildMapMessage1) + 4;
			int buildMapW2 = fr.getStringWidth(buildMapMessage2) + 4;
			int buildMapW = Math.max(buildMapW1, buildMapW2);
			Gui.drawRect(width/2-buildMapW/2, yShift, width/2+buildMapW/2, yShift + 28, 0x50000000);
			fr.drawStringWithShadow(buildMapMessage1, width/2-buildMapW1/2+2, yShift + 2, 0xfda129);
			fr.drawStringWithShadow(buildMapMessage2, width/2-buildMapW2/2+2, yShift + 13, 0xffdbad);
		}
		
		// MAP INFOS
		
		Gui.drawRect(width/2+90, 2, width-2, 35 + (tab ? fr.getStringWidth(comment) / (commentWidth)*10 : -5), 0x50000000);
		
		if(tab) {
			fr.drawSplitString(comment, width/2+94, 25, commentWidth, 0xFFFFFF);
		}
		
		fr.drawStringWithShadow(name, width/2+140 - fr.getStringWidth(name)/2, 5, 0x72ff4c);
		fr.drawStringWithShadow(author, width/2+127 - (fr.getStringWidth(author) > 76 ? fr.getStringWidth(author) - 76 : 0), 15, 0xFFFFFF);
	}
	
	private void renderAllBonus() {
		int shift = 0;
		for(Entry<Bonus, Integer> entry : Bonus.getPlayersBonus(mc.thePlayer).entrySet()) {
			Bonus bonus = entry.getKey();
			int amount = entry.getValue();
			renderBonus(15, 30+shift, 75, 26, bonus.name().substring(0, 1).toUpperCase()+bonus.name().substring(1).toLowerCase(), bonus.getItem().itemID, amount);
			shift+=28;
		}
	}
	
	private void renderBonus(int x, int y, int width, int height, String name, int itemId, int itemCount) {
		GL11.glColor4f(1, 1, 1, 1);
		
		int texture;
		int textureId;
		
		if(itemCount < 1) {
			return;
		}
		
//		if(itemId < 256) {
//			texture = mc.renderEngine.getTexture("/terrain.png");
//			textureId = Block.blocksList[itemId].blockIndexInTexture;
//		} else {
//			texture = mc.renderEngine.getTexture("/gui/items.png");
//			textureId = Item.itemsList[itemId].getIconFromDamage(0);
//		}
		
		int maxWidth = 160, maxHeight = 32;
		
//		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/achievement/bg.png"));
		drawTexturedModalRect(x, y, 96, 202, width-4, height-4); 
		drawTexturedModalRect(x+width-4, y, 96+maxWidth-4, 202, 4, height-4);
		drawTexturedModalRect(x, y+height-4, 96, 202+maxHeight-4, width-4, 4);
		drawTexturedModalRect(x+width-4, y+height-4, 96+maxWidth-4, 202+maxHeight-4, 4, 4);
		
		drawTexturedModalRect(x+2, y+2, 2, 204, 22, 22);
		
		drawString(mc.fontRenderer, name, x+26, y+5, 0xDDDDDD);
		if(itemCount > 1) {
			drawString(mc.fontRenderer, "x"+itemCount, x+30, y+14, 0xfed2a7);
		}
		
		GL11.glColor4f(1, 1, 1, 1);
		
//		mc.renderEngine.bindTexture(texture);
//		int u = (textureId % 16) * 16;
//		int v = (textureId / 16) * 16;
//		drawTexturedModalRect(x+5, y+5, u, v, 16, 16);
		drawTexturedModalRect(x+5, y+5, 0, 0, 16, 16);
		
	}
	
	private void renderPlayerScores(int i, int j, float f) {
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int k = scaledresolution.getScaledWidth();
        
        NetClientHandler netclienthandler = ((EntityClientPlayerMP)mc.thePlayer).sendQueue;
        @SuppressWarnings("unchecked")
		List<GuiPlayerInfo> list = netclienthandler.playerInfoList;
        int j3 = netclienthandler.currentServerMaxPlayers;
        int i4 = j3;
        int k4 = 1;
        for(; i4 > 20; i4 = ((j3 + k4) - 1) / k4)
        {
            k4++;
        }

        int k5 = 300 / k4;
        if(k5 > 150)
        {
            k5 = 150;
        }
        int j6 = (k - k4 * k5) / 2;
        byte byte2 = 10;
        for(int k7 = 0; k7 < j3; k7++)
        {
            int i8 = j6 + (k7 % k4) * k5;
            int l8 = byte2 + (k7 / k4) * 9;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            if(k7 >= list.size())
            {
                continue;
            }
            GuiPlayerInfo guisavinglevelstring = (GuiPlayerInfo)list.get(k7);
            EntityPlayer player = mc.theWorld.getPlayerEntityByName(guisavinglevelstring.name);
            if(player != null) {
                if(player.posY > 70) {
                	mc.fontRenderer.drawStringWithShadow(player.username, i8+k5-150, l8, 0xb61818);
                }
                
                int score = BomberMineFML.instance.getScore(player);
                int scoreShift = score < 0 ? mc.fontRenderer.getStringWidth("-") : 0;
                mc.fontRenderer.drawStringWithShadow(""+score, i8+k5-50-scoreShift, l8, 0xffffff);
            }
        }
	}
}
