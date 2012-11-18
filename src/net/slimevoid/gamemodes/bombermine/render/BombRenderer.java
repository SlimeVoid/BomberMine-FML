package net.slimevoid.gamemodes.bombermine.render;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.slimevoid.gamemodes.bombermine.BomberMineFML;
import net.slimevoid.gamemodes.bombermine.Utils;
import net.slimevoid.gamemodes.bombermine.blocks.BlockBomb;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BombRenderer implements ISimpleBlockRenderingHandler {
	
	public static int renderId;
	
	public BombRenderer() {
		renderId = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if(block instanceof BlockBomb) {
			GL11.glScaled(1.35, 1.35, 1.35);
			GL11.glTranslated(0, 0.15, 0);
			GL11.glRotatef(-90, 0, 1, 0);
			Block main = Block.obsidian, wool = Block.cloth;
			int initialTex = main.blockIndexInTexture;
			main.blockIndexInTexture = block.blockIndexInTexture;
			main.setBlockBounds(0.25F, 0.125F, 0.125F, 0.75F, 0.625F, 0.875F);
			renderer.renderBlockAsItem(main, 0, 1F);
			main.setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 0.75F, 0.75F);
			renderer.renderBlockAsItem(main, 0, 1F);
			main.setBlockBounds(0.125F, 0.125F, 0.25F, 0.875F, 0.625F, 0.75F);
			renderer.renderBlockAsItem(main, 0, 1F);
			main.setBlockBounds(0, 0, 0, 1, 1, 1);
			if(block != BomberMineFML.instance.BOMB_LASER) {
				wool.setBlockBounds(0.4375F, 0.75F, 0.5F, 0.5F, 0.9375F, 0.5625F);
				renderer.renderBlockAsItem(wool, 0, 1);
				wool.setBlockBounds(0.5F, 0.9375F, 0.5F, 0.5625F, 1, 0.5625F);
				renderer.renderBlockAsItem(wool, 0, 1);
				wool.setBlockBounds(0, 0, 0, 1, 1, 1);
			}
			
			main.blockIndexInTexture = initialTex;
			return;
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Block wool = Block.cloth;
		wool.setBlockBounds(.1F, .1F, .1F, .9F, .9F, .9F);
		block.setBlockBounds(.1F, .1F, .1F, .9F, .9F, .9F);
		Utils.renderer = renderer;
		Utils.setRendererBounds(0.25F, 0.125F, 0.125F, 0.75F, 0.625F, 0.875F);
		renderer.renderStandardBlock(block, x, y, z);
		Utils.setRendererBounds(0.25F, 0F, 0.25F, 0.75F, 0.75F, 0.75F);
		renderer.renderStandardBlock(block, x, y, z);
		Utils.setRendererBounds(0.125F, 0.125F, 0.25F, 0.875F, 0.625F, 0.75F);
		renderer.renderStandardBlock(block, x, y, z);
		Utils.setRendererBounds(0, 0, 0, 1, 1, 1);
		if(block != BomberMineFML.instance.BOMB_LASER) {
			int metadata = world.getBlockMetadata(x, y, z);
			if(metadata < 3) {
				Utils.setRendererBounds(0.4375F, 0.75F, 0.5F, 0.5F, 0.9375F, 0.5625F);
				renderer.renderStandardBlock(wool, x, y, z);
			} else if(metadata < 4) {
				Utils.setRendererBounds(0.4375F, 0.75F, 0.5F, 0.5F, 0.875F, 0.5625F);
				renderer.renderStandardBlock(wool, x, y, z);
			} else {
				Utils.setRendererBounds(0.4375F, 0.75F, 0.5F, 0.5F, 0.8125F, 0.5625F);
				renderer.renderStandardBlock(wool, x, y, z);
			}
			if(metadata < 2) {
				Utils.setRendererBounds(0.5F, 0.9375F, 0.5F, 0.5625F, 1, 0.5625F);
				renderer.renderStandardBlock(wool, x, y, z);
			}
			if(metadata < 1) {
				Utils.setRendererBounds(0.5625F, 1F, 0.5F, 0.625F, 1.0625F, 0.5625F);
				renderer.renderStandardBlock(wool, x, y, z);
			}
			Utils.setRendererBounds(0, 0, 0, 1, 1, 1);
		}
		wool.setBlockBounds(0, 0, 0, 1, 1, 1);
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
		return true;
	}
	
	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderId;
	}
}
