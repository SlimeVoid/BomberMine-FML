package net.slimevoid.gamemodes.bombermine.map;

public class GenRandom extends MapGeneratorFunc {

	public GenRandom(int xStart, int zStart, int xSize, int zSize, int block, boolean flag) {
		super(xStart, zStart, xSize, zSize, block, flag);
	}

	@Override
	public void generate() {
		genRandomContent(xStart, zStart, xSize, zSize, block);
	}

}
