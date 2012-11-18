package net.slimevoid.gamemodes.bombermine.render;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.slimevoid.gamemodes.bombermine.bonus.Drop;
import net.slimevoid.gamemodes.bombermine.tileentities.TileEntityBonus;

import org.lwjgl.opengl.GL11;

public class BonusRenderer extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		TileEntityBonus tile = (TileEntityBonus) tileentity;
		
		if(Item.itemsList[tile.getBonusItemID()] == null) {
			return;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y+0.0001, z);
		
		Tessellator tess = Tessellator.instance;
		
		//RENDER BASE
		double yShift = 0.0625 * 2;
		{	bindTextureByName("/terrain.png");
		
			double margin = 0.0625;
			
			int texture = 9;
			int u = texture % 16, v = texture / 16;
			double uMin = u/16D, uMax = (u+0.001)/16D;
			double vMin = v/16D, vMax = (v+0.001)/16D;
			
			double xMin = 0 + margin, xMax = 1 - margin;
			double yMin = 0			, yMax = yShift;
			double zMin = 0 + margin, zMax = 1 - margin;
			
			int millisPerFlick = 1200;
			float flickCoef = ((System.currentTimeMillis() + (tileentity.xCoord + tileentity.zCoord)*millisPerFlick/5) % millisPerFlick) / (float)millisPerFlick;
			flickCoef = flickCoef > 0.5F ? 1F - (flickCoef-0.5F)*2 : flickCoef * 2;
			flickCoef = flickCoef * 0.5F + 0.5F;
			
			GL11.glColor4f(0F, 0F, 0F, 0.8F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			tess.startDrawingQuads();
			tess.setNormal(0, 1, 0);
			tess.addVertexWithUV(xMin, yMin, zMax, uMax, vMin);
			tess.addVertexWithUV(xMax, yMin, zMax, uMax, vMax);
			tess.addVertexWithUV(xMax, yMin, zMin, uMin, vMax);
			tess.addVertexWithUV(xMin, yMin, zMin, uMin, vMin);
			tess.draw();
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			Drop drop = Drop.getDropByItemID(tile.getBonusItemID());
			if(drop != null) {
				GL11.glColor4f(drop.getR() * flickCoef, drop.getG() * flickCoef, drop.getB() * flickCoef, 1F);
			}
			
			tess.startDrawingQuads();
			tess.setNormal(0, 1, 0);
			tess.addVertexWithUV(xMin, yMax, zMax, uMax, vMin);
			tess.addVertexWithUV(xMax, yMax, zMax, uMax, vMax);
			tess.addVertexWithUV(xMax, yMax, zMin, uMin, vMax);
			tess.addVertexWithUV(xMin, yMax, zMin, uMin, vMin);
			
			tess.setNormal(-1, 0, 0);
			tess.addVertexWithUV(xMin, yMin	, zMin, uMin, vMax);
			tess.addVertexWithUV(xMin, yMin	, zMax, uMax, vMax);
			tess.addVertexWithUV(xMin, yMax	, zMax, uMax, vMin);
			tess.addVertexWithUV(xMin, yMax	, zMin, uMin, vMin);
			
			tess.setNormal(1, 0, 0);
			tess.addVertexWithUV(xMax, yMax	, zMax, uMax, vMin);
			tess.addVertexWithUV(xMax, yMin	, zMax, uMax, vMax);
			tess.addVertexWithUV(xMax, yMin	, zMin, uMin, vMax);
			tess.addVertexWithUV(xMax, yMax	, zMin, uMin, vMin);
			
			tess.setNormal(0, 0, -1);
			tess.addVertexWithUV(xMin, yMin	, zMax, uMin, vMax);
			tess.addVertexWithUV(xMax, yMin	, zMax, uMax, vMax);
			tess.addVertexWithUV(xMax, yMax	, zMax, uMax, vMin);
			tess.addVertexWithUV(xMin, yMax	, zMax, uMin, vMin);
			
			tess.setNormal(0, 0, 1);
			tess.addVertexWithUV(xMax, yMax	, zMin, uMax, vMin);
			tess.addVertexWithUV(xMax, yMin	, zMin, uMax, vMax);
			tess.addVertexWithUV(xMin, yMin	, zMin, uMin, vMax);
			tess.addVertexWithUV(xMin, yMax	, zMin, uMin, vMin);
			
			tess.draw();
		}
		//RENDER BASE END
		if(tile.getBonusItemID() > 0 ) {
			if(tile.getBonusItemID() < 256 && tile.getBonusItemID() != Block.fire.blockID) { //RENDER BLOCK
				GL11.glDisable(GL11.GL_BLEND);
				
				double scale = 0.5;
				
				GL11.glTranslated(0.5, 0.5 * scale + yShift, 0.5);
				GL11.glScaled(scale, scale, scale);
				
				RenderBlocks rb = new RenderBlocks(tile.worldObj);
				Block block = Block.blocksList[tile.getBonusItemID()];
				rb.renderBlockAsItem(block, 0, f);
			//RENDER BLOCK END
			} else { //RENDER ITEM
				int texture;
				if(tile.getBonusItemID() > 256) {
					bindTextureByName("/gui/items.png");
					texture = Item.itemsList[tile.getBonusItemID()] != null ? Item.itemsList[tile.getBonusItemID()].getIconFromDamage(0) : 0;
				} else {
					Block block = Block.blocksList[tile.getBonusItemID()];
					texture = block != null ? block.blockIndexInTexture : 0;
				}
				
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glColor3f(1F, 1F, 1F);
			
				double yMin = yShift, yMax = yMin + 0.0625 * 1;
				
				double margin = 0.0625 * 3;
				
				int u = texture % 16, v = texture / 16;
				double uMin = u/16D, uMax = (u+1)/16D;
				double vMin = v/16D, vMax = (v+1)/16D;
				
				double xMin = 0 + margin, xMax = 1 - margin;
				double zMin = 0 + margin, zMax = 1 - margin;
				
				tess.startDrawingQuads();
				tess.setNormal(0, 1, 0);
				tess.addVertexWithUV(xMin, yMax, zMax, uMax, vMax);
				tess.addVertexWithUV(xMax, yMax, zMax, uMax, vMin);
				tess.addVertexWithUV(xMax, yMax, zMin, uMin, vMin);
				tess.addVertexWithUV(xMin, yMax, zMin, uMin, vMax);
				tess.draw();
				
				//EXTRUDE
				int width = 16;
				double totalX = (xMax - xMin);
				double uShift = 1D/256D/2D;
				
				tess.startDrawingQuads();
				tess.setNormal(-1F, 0.0F, 0.0F);
				for(int pos = 0; pos < width ; pos++) {
					double xPos = ((double)pos / (double)width);
				    double v2 = (vMin + (vMax - vMin) * (xPos)) + uShift;
				    double x2 = (totalX  * (1D - xPos)) + (1 - totalX)/2 - (totalX / 16);
				    tess.addVertexWithUV(x2, yMin, zMin, uMin, v2);
				    tess.addVertexWithUV(x2, yMin, zMax, uMax, v2);
				    tess.addVertexWithUV(x2, yMax, zMax, uMax, v2);
				    tess.addVertexWithUV(x2, yMax, zMin, uMin, v2);
				}
				tess.draw();
				
				tess.startDrawingQuads();
				tess.setNormal(1F, 0.0F, 0.0F);
				for(int pos = 0; pos < width ; pos++) {
					double xPos = (double)pos / (double)width;
				    double v2 = (vMin + (vMax - vMin) * xPos) + uShift;
				    double x2 = (totalX  * (1D - xPos)) + (1 - totalX)/2 + (totalX / (double)width)  - (totalX / 16);
				    tess.addVertexWithUV(x2, yMin, zMax, uMax, v2);
				    tess.addVertexWithUV(x2, yMin, zMin, uMin, v2);
				    tess.addVertexWithUV(x2, yMax, zMin, uMin, v2);
				    tess.addVertexWithUV(x2, yMax, zMax, uMax, v2);
				}
				tess.draw();
				
				tess.startDrawingQuads();
				tess.setNormal(0F, 0F, 1F);
				for(int pos = 0; pos < width ; pos++) {
					double xPos = (double)pos / (double)width;
				    double u2 = (uMin + (uMax - uMin) * xPos) + uShift;
				    double z2 = (totalX  * xPos) + (1 - totalX)/2;
				    tess.addVertexWithUV(xMax, yMin, z2, u2, vMin);
				    tess.addVertexWithUV(xMin, yMin, z2, u2, vMax);
				    tess.addVertexWithUV(xMin, yMax, z2, u2, vMax);
				    tess.addVertexWithUV(xMax, yMax, z2, u2, vMin);
				}
				tess.draw();
				
				tess.startDrawingQuads();
				tess.setNormal(0F, 0F, 1F);
				for(int pos = 0; pos < width ; pos++) {
					double xPos = (double)pos / (double)width;
				    double u2 = (uMin + (uMax - uMin) * xPos) + uShift;
				    double z2 = (totalX  * xPos) + (1 - totalX)/2 + (totalX / (double)width);
				    tess.addVertexWithUV(xMin, yMin, z2, u2, vMax);
				    tess.addVertexWithUV(xMax, yMin, z2, u2, vMin);
				    tess.addVertexWithUV(xMax, yMax, z2, u2, vMin);
				    tess.addVertexWithUV(xMin, yMax, z2, u2, vMax);
				}
				tess.draw();
				//EXTRUDE END
			//RENDER ITEM END
			}
		}
		
		GL11.glPopMatrix();
	}
}
