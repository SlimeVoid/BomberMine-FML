package net.slimevoid.gamemodes.bombermine.proxy;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.slimevoid.gamemodes.bombermine.GameSession;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class CommonTickHandler implements ITickHandler {
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.equals(EnumSet.of(TickType.SERVER))) {
			onTickInGame();
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return null;
	}
	
	private void onTickInGame() {
		List<GameSession> sessions = new ArrayList<>(GameSession.getSessionsList());
		for(GameSession session : sessions) {
			session.onTick();
		}
	}
}
