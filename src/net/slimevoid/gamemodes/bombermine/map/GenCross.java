package net.slimevoid.gamemodes.bombermine.map;

public class GenCross extends MapGeneratorFunc {

	public GenCross(int xStart, int zStart, int xSize, int zSize, int block, boolean flag) {
		super(xStart, zStart, xSize, zSize, block, flag);
	}

	@Override
	public void generate() {
		genCrossContent(xStart, zStart, xSize, zSize, flag, block);
	}

}
