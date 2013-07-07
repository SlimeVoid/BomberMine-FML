package net.slimevoid.gamemodes.bombermine.bonus;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum Bonus {
	POWER(new ItemStack(Block.fire)			, 9	, 1	, 64	, 1		, 0xdcab26),
	TROUGH(new ItemStack(Item.book)			, 10, 0 , 1		, 0.05	, 0x06bcff),
	SPEED(new ItemStack(Item.bootsDiamond)	, 11, 0 , 1 	, 0.1	, 0x06bcff),
	SLOW(new ItemStack(Item.bootsLeather)	, 12, 0 , 1		, 0.4	, 0xff6f06);
	
	Bonus(ItemStack item, int posInInventory, int startAmount, int max, double dropRate, int color) {
		this.item = item;
		this.posInInventory = posInInventory;
		this.startAmount = startAmount+1;
		this.max = max;
		
		if(dropRate > 0) {
			new Drop(getItem().itemID, dropRate, color);
		}
	}
	
	public static boolean hasPlayerAllBonus(EntityPlayer player, Bonus[] allBonus) {
		Map<Bonus, Integer> playerBonus = getPlayersBonus(player);
		for(Bonus bonus : allBonus) {
			if(!playerBonus.containsKey(bonus)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean hasPlayerBonus(EntityPlayer player, Bonus bonus) {
		return getPlayersBonus(player).containsKey(bonus);
	}
	
	public static int getPlayerBonusAmount(EntityPlayer player, Bonus bonus) {
		Integer bonusAmount = getPlayersBonus(player).get(bonus);
		return bonusAmount != null ? bonusAmount : 0;
	}
	
	public static Map<Bonus, Integer> getPlayersBonus(EntityPlayer player) {
		Map<Bonus, Integer> map = new HashMap<Bonus, Integer>();
		if (player != null && player.inventory != null){
			for(Bonus bonus : Bonus.values()) {
				ItemStack inv = player.inventory.mainInventory[bonus.getPosInInventory()];
				if(inv != null && inv.itemID == bonus.getItem().itemID && inv.stackSize > 1) {
					map.put(bonus, inv.stackSize-1);
				}
			}
		}
		return map;
	}
	
	public static Bonus getBonusByPos(int pos) {
		return posToBonus.containsKey(pos) ? posToBonus.get(pos) : null;
	}
	
	public static Bonus getBonusById(int id) {
		return idToBonus.containsKey(id) ? idToBonus.get(id) : null;
	}
	
	public ItemStack getItem() {
		return item;
	}

	public int getPosInInventory() {
		return posInInventory;
	}

	public int getMax() {
		return max;
	}

	public int getStartAmount() {
		return startAmount;
	}

	private final ItemStack item;
	private final int posInInventory;
	private final int max;
	private final int startAmount;
	
	private static final Map<Integer, Bonus> posToBonus = new HashMap<Integer, Bonus>();
	private static final Map<Integer, Bonus> idToBonus = new HashMap<Integer, Bonus>();
	
	static {
		for(Bonus bonus : Bonus.values()) {
			posToBonus.put(bonus.getPosInInventory(), bonus);
			idToBonus.put(bonus.getItem().itemID, bonus);
		}
	}
}