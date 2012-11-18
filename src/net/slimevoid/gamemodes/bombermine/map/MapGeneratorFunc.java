package net.slimevoid.gamemodes.bombermine.map;

import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;


public abstract class MapGeneratorFunc {
	
	public MapGeneratorFunc(int xStart, int zStart, int xSize, int zSize, int block, boolean flag) {
		this.xStart = xStart;
		this.zStart = zStart;
		this.xSize = xSize;
		this.zSize = zSize;
		this.block = block;
		this.world = MinecraftServer.getServer().worldServerForDimension(0);
		this.flag = flag;
	}
	
	public abstract void generate();
	
	public static MapGeneratorFunc getGenerator(int id, int xPos, int zPos, int xSize, int zSize, int blockID, boolean flag) {
		MapGeneratorFunc generator = null;
		switch(id) {
		case 1:
			generator = new GenRandom(xPos, zPos, xSize, zSize, blockID, flag);
			break;
			
		case 2:
			generator = new GenBox(xPos, zPos, xSize, zSize, blockID, flag);
			break;
			
		case 3:
			generator = new GenCross(xPos, zPos, xSize, zSize, blockID, flag);
			break;
			
		case 4:
			generator = new GenLine(xPos, zPos, xSize, zSize, blockID, flag);
			break;
			
		case 5:
			generator = new GenDot(xPos, zPos, xSize, zSize, blockID, flag);
			break;
		}
		return generator;
	}
	
	protected void setBlock(final int x, final int z, final int block) {
		if (isInMap(x, z)) {
			map.setBlock(x, z, block);
		}
	}
	
	protected void genRandomContent(int xStart, int zStart, int xSize, int zSize, int block) {
		double ratio = getMapSize() / 32.0f;
		xSize = (int) Math.round(xSize*ratio);
		zSize = (int) Math.round(zSize*ratio);
		xStart = (int) Math.round(xStart*ratio);
		zStart = (int) Math.round(zStart*ratio);
		
		for(int x = xStart; x < xStart + xSize + 1; x ++) {
			for(int z = zStart; z < zStart + zSize + 1; z ++) {
				if(world.rand.nextBoolean()){
					setBlock(x, z, block);
				} 
			}
		}
	}

	protected void genBoxContent(int xStart, int zStart, int xSize, int zSize, boolean isFilled, int block) {
		double ratio = getMapSize() / 32.0f;
		xSize = (int) Math.round(xSize*ratio);
		zSize = (int) Math.round(zSize*ratio);
		xStart = (int) Math.round(xStart*ratio);
		zStart = (int) Math.round(zStart*ratio);
		
		for(int x = xStart; x < xStart + xSize + 1; x ++){
			for(int z = zStart; z < zStart + zSize + 1; z ++) {
				if (isFilled){
					setBlock(x, z, block);
				} else {
					if (x == xStart || z == zStart || x == xStart + xSize || z == zStart + zSize) {
						setBlock(x, z, block);
					}
				}
			}
		}
	}


	protected void genCrossContent(int xStart, int zStart, int xSize, int zSize, boolean isDiag, int block) {
		if (isDiag) {
			genLineContent(xStart, zStart, xSize, zSize, block);
			genLineContent(xStart + xSize, zStart, -xSize, zSize, block);
		} else {
			genLineContent(xStart, zStart + zSize / 2, xSize, 0, block);
			genLineContent(xStart + xSize / 2, zStart, 0, zSize, block);
		}

	}

	protected void genLineContent(int xStart, int zStart, int xSize, int zSize, int block) {
		double ratio = getMapSize() / 32.0f;
		xSize = (int) Math.round(xSize*ratio);
		zSize = (int) Math.round(zSize*ratio);
		xStart = (int) Math.round(xStart*ratio);
		zStart = (int) Math.round(zStart*ratio);
		
		int x = xStart;
		int y = zStart;
		int dx = xSize;
		int dy = zSize;

		int xinc;
		int yinc;
		int cumul;

		if (dx > 0) {
			xinc = 1;
		} else {
			xinc = -1;
		}

		if (dy > 0) {
			yinc = 1;
		} else {
			yinc = -1;
		}

		dx = (int) MathHelper.abs(dx);
		dy = (int) MathHelper.abs(dy);
		setBlock(x,y, block);

		if (dx > dy) {
			cumul = dx / 2;
			for(int i = 1; i < dx + 1; i ++) { 
				x = x + xinc;
				cumul  = cumul + dy;
				if (cumul >= dx) {
					cumul = cumul - dx;
					y  = y + yinc;
				} 
				setBlock(x,y, block);
			} 
		} else {
			cumul = dy / 2;
			for(int i = 1; i < dy + 1; i ++) {
				y = y + yinc;
				cumul = cumul + dx;
				if (cumul >= dy) {
					cumul = cumul - dy;
					x = x + xinc;
				} 
				setBlock(x, y, block); 
			}   
		}  

	}
	
	public BombermanMap getMap() {
		return map;
	}

	public void setMap(BombermanMap map) {
		this.map = map;
	}
	
	protected boolean isInMap(int x, int z) {
		return (x >= 1 && x <= getMapSize() - 1 && z >= 1 && z <= getMapSize() - 1);
	}
	
	protected int getMapSize() {
		return getMap().mapSize+1;
	}

	protected BombermanMap map = null;

	private World world;
	
	protected final Random rand = new Random();
	
	protected final int xStart, zStart, xSize, zSize, block;
	protected final boolean flag;
}
