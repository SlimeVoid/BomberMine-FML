package net.slimevoid.gamemodes.bombermine.map;

public class GenDot extends MapGeneratorFunc {


	public GenDot(int xStart, int zStart, int xSize, int zSize, int block, boolean flag) {
		super(xStart, zStart, xSize, zSize, block, flag);
	}

	@Override
	public void generate() {
		double ratio = getMapSize() / 32.0f;
		setBlock((int)Math.round(xStart * ratio), (int)Math.round(zStart * ratio), block);
	}

}
