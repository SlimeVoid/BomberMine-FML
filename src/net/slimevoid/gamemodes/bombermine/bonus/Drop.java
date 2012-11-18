package net.slimevoid.gamemodes.bombermine.bonus;

import java.util.ArrayList;
import java.util.List;

public class Drop {
	public Drop(int itemID, double rate, int color) {
		this.itemID = itemID;
		this.rate = rate;
		
		this.r = ((color & 0xFF0000) >> 16) / 256F;
		this.g = ((color & 0x00FF00) >> 8) / 256F;
		this.b = ((color & 0x0000FF) >> 0) / 256F;
		
		drops.add(this);
	}
	
	public int getItemID() {
		return itemID;
	}

	public double getRate() {
		return rate;
	}
	
	public static List<Drop> getDrops() {
		return drops;
	}
	
	public static Drop getDropByItemID(int itemID) {
		for(Drop drop : drops) {
			if(drop.itemID == itemID) {
				return drop;
			}
		}
		return null;
	}
	
	public float getR() {
		return r;
	}

	public float getG() {
		return g;
	}

	public float getB() {
		return b;
	}

	private final int itemID;
	private final double rate;
	private final float r, g, b;
	
	private static final List<Drop> drops = new ArrayList<Drop>();
}
