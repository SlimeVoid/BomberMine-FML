package net.slimevoid.gamemodes.bombermine.map;

public class GenLine extends MapGeneratorFunc {

	public GenLine(int xStart, int zStart, int xSize, int zSize, int block, boolean flag) {
		super(xStart, zStart, xSize, zSize, block, flag);
	}

	@Override
	public void generate() {
		genLineContent(xStart, zStart, xSize, zSize, block);
	}
}
