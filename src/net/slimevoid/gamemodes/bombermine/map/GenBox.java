package net.slimevoid.gamemodes.bombermine.map;

public class GenBox extends MapGeneratorFunc {

	public GenBox(int xStart, int zStart, int xSize, int zSize, int block, boolean flag) {
		super(xStart, zStart, xSize, zSize, block, flag);
	}

	@Override
	public void generate() {
		genBoxContent(xStart, zStart, xSize, zSize, flag, block);
	}

}
