//package net.slimevoid.gamemodes.bombermine.tileentities;
//
//import java.util.List;
//
//import net.minecraft.src.EntityClientPlayerMP;
//import net.minecraft.src.EntityPlayer;
//import net.minecraft.src.FontRenderer;
//import net.minecraft.src.GuiPlayerInfo;
//import net.minecraft.src.ItemStack;
//import net.minecraft.src.ModLoader;
//import net.minecraft.src.NetClientHandler;
//import net.minecraft.src.RenderManager;
//import net.minecraft.src.TileEntity;
//import net.minecraft.src.TileEntitySpecialRenderer;
//
//import org.lwjgl.opengl.GL11;
//
//public class PoduimRenderer extends TileEntitySpecialRenderer {
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
//		int metadata = tileentity.worldObj.getBlockMetadata(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
//		
//		NetClientHandler netclienthandler = ((EntityClientPlayerMP)ModLoader.getMinecraftInstance().thePlayer).sendQueue;
//        List<GuiPlayerInfo> list = netclienthandler.playerNames;
//        
//        if(metadata >= list.size() || list.get(metadata) == null) {
//        	return;
//        }
//        
//        EntityPlayer player = ModLoader.getMinecraftInstance().theWorld.getPlayerEntityByName(list.get(metadata).name);
//        
//        if(player == null) {
//        	return;
//        }
//        
//		float pYaw = player.rotationYaw, pPrevYaw = player.prevRotationYaw, pYawO = player.renderYawOffset, pPrevYawO = player.prevRenderYawOffset;
//		
//		player.rotationYaw = 270;
//		player.prevRotationYaw = 270;
//		player.renderYawOffset = 270;
//		player.prevRenderYawOffset = 270;
//		
//		float pPitch = player.rotationPitch, pPrevPitch = player.prevRotationPitch;
//		float pSwing = player.swingProgress, pPrevSwing = player.prevSwingProgress;
//		
//		player.rotationPitch = 0;
//		player.prevRotationPitch = 0;
//		
//		player.prevDistanceWalkedModified = 0;
//		player.distanceWalkedModified = 0;
//		player.prevSwingProgress = 0;
//		player.swingProgress = 0;
//		
//		ItemStack last = player.inventory.mainInventory[player.inventory.currentItem];
//		
//		player.inventory.mainInventory[player.inventory.currentItem] = null;
//		
//		float prevQ = player.field_705_Q, prevR = player.field_704_R;
//		
//		player.field_705_Q = 0;
//		player.field_704_R = 0;
//		
//		boolean lastIsDead = player.isDead;
//		player.isDead = false;
//		
//		String username = player.username;
//		player.username = "";
//		
//		float scale = 2F;
//		
//		GL11.glPushMatrix();
//		if(player == ModLoader.getMinecraftInstance().thePlayer) {
//			GL11.glTranslated(0, (player.height-player.getEyeHeight()-0.1)*scale, 0);
//		}
//		GL11.glTranslated(x+1, y, z+1);
//		GL11.glScaled(scale, scale, scale);
//		
//		RenderManager renderManager = RenderManager.instance;
//		renderManager.renderEntityWithPosYaw(player, 0, 0, 0, f, 0F);
//		player.username = username;
//		GL11.glPopMatrix();
//		
//		GL11.glPushMatrix();
//		
//        FontRenderer fontrenderer = getFontRenderer();
//        float f4 = 0.01666667F * scale;
//        GL11.glTranslated(x + 2.01, y - 1.5F, z + 1);
//        GL11.glScalef(f4, -f4, f4);
//        GL11.glRotatef(90, 0, 1, 0);
//        GL11.glNormal3f(1.0F, 0.0F, 0.0F);
//        GL11.glDepthMask(false);
//        int color = 0xFFFFFF;
//        fontrenderer.drawString(player.username, -fontrenderer.getStringWidth(player.username)/2, 0, color);
//        fontrenderer.drawString(""+(metadata+1), -fontrenderer.getStringWidth(""+(metadata+1))/2, -10, color);
//
//        GL11.glDepthMask(true);
//        
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        
//        GL11.glPopMatrix();	
//        
//		player.hurtTime = 0;
//		player.deathTime = 0;
//		
//		player.rotationYaw = pYaw;
//		player.prevRotationYaw = pPrevYaw;
//		player.renderYawOffset = pYawO;
//		player.prevRenderYawOffset = pPrevYawO;
//		
//		player.rotationPitch = pPitch;
//		player.prevRotationPitch = pPrevPitch;
//		
//		player.prevSwingProgress = pPrevSwing;
//		player.swingProgress = pSwing;
//		
//		player.inventory.mainInventory[player.inventory.currentItem] = last;
//		
//		player.field_705_Q = prevQ;
//		player.field_704_R = prevR;
//		
//		player.isDead = lastIsDead;
//	}
//
//}
