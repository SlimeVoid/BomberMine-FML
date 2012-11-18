package net.slimevoid.gamemodes.bombermine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.WorldServer;

public class BombermineMainSession extends GameSession {
	
	public BombermineMainSession() {
		super(-1);
		
		PTS_KILL = BomberMineFML.ptsForKill;
		PTS_SUICIDE = BomberMineFML.ptsForSuicide;
	}

	@Override
	public void onTick() {
		super.onTick();
		if (gameSession == null) {
			gameSession = new BombermanGameSession();
		}
		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(0);
		ws.setWorldTime(6000);
		ws.getWorldInfo().setRainTime(0);
		ws.getWorldInfo().setRaining(false);
		ws.getWorldInfo().setThunderTime(0);
		ws.getWorldInfo().setThundering(false);
	}
	
	public void registerKill(EntityPlayerMP victim, List<EntityPlayerMP> actorsToNotModify) {
		List<EntityPlayerMP> actors = new ArrayList<EntityPlayerMP>(actorsToNotModify);
		if(actors.contains(victim)) {
			actors.remove(victim);
		}
		
		addToScore(victim, -PTS_KILL); 
		
		int ptsLeft = PTS_KILL;
		int actorsLeft = actors.size();
		for(EntityPlayerMP actor : actors) {
			int toGive = ptsLeft/actorsLeft + ((ptsLeft % actorsLeft) > 0 ? 1 : 0);
			addToScore(actor, toGive);
			
			ptsLeft -= toGive;
			actorsLeft --;
		}
//		Packet230ModLoader packet = new Packet230ModLoader(); TODO kill packet
//		packet.dataInt = new int[]{2, 0};
//		packet.dataFloat = new float[]{(float) victim.posX, (float) victim.posY, (float) victim.posZ};
//		EntityPlayerMP lastActor = actors.get(actors.size()-1);
//		packet.dataString = new String[]{victim.username, lastActor == null ? "" : lastActor.username};
//		
//		ModLoaderMp.sendPacketToAll(mod_BomberMan.instance, packet);
	}
	
	public void registerSuicide(EntityPlayerMP victim) {
		int dynamicPtsSuicide = PTS_SUICIDE / gameSession.getPlayersAtStartOfTheRound();
		
		List<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>(gameSession.getInGamePlayers());
		
		players.remove(victim);
		
		for(EntityPlayerMP player : players) {
			addToScore(player, dynamicPtsSuicide);
		}
		addToScore(victim, -dynamicPtsSuicide*players.size());
		
//		Packet230ModLoader packet = new Packet230ModLoader(); TODO sucide packet
//		packet.dataInt = new int[]{2, 0};
//		packet.dataFloat = new float[]{(float) victim.posX, (float) victim.posY, (float) victim.posZ};
//		packet.dataString = new String[]{victim.username, victim.username};
//		
//		ModLoaderMp.sendPacketToAll(mod_BomberMan.instance, packet);
	}
	
	private void addToScore(EntityPlayerMP player, int scoreToAdd) {
		setScore(player, getScore(player) + scoreToAdd);
	}
	
	private void setScore(EntityPlayerMP player, int score) {
		if(player == null) {
			return;
		}
		scores.put(player.username, score);
//		Packet230ModLoader packet = new Packet230ModLoader(); TODO score packet
//		packet.dataInt = new int[]{1, score};
//		packet.dataString = new String[]{player.username};
//		ModLoaderMp.sendPacketToAll(mod_BomberMan.instance, packet);
	}
	
	public int getScore(EntityPlayerMP player) {
		if(player != null && scores.containsKey(player.username)) {
			return scores.get(player.username);
		}
		return 0;
	}
	
	public List<String> getPlayersWithHighestScore() {
		List<String> players = new ArrayList<String>();
		
		int maxScore = 0;
		for(Entry<String, Integer> entry : scores.entrySet()) {
			if(entry.getValue() > maxScore) {
				players.clear();
				maxScore = entry.getValue();
				players.add(entry.getKey());
			} else if(entry.getValue() == maxScore) {
				players.add(entry.getKey());
			}
		}
		
		return players;
	}
	
	public void resetScores() {
		for(@SuppressWarnings("unused") Entry<String, Integer> entry : scores.entrySet()) {
//			Packet230ModLoader packet = new Packet230ModLoader(); TODO score packet
//			packet.dataInt = new int[]{1, 0};
//			packet.dataString = new String[]{entry.getKey()};
//			ModLoaderMp.sendPacketToAll(mod_BomberMan.instance, packet);
		}
		scores.clear();
	}
	
	private Map<String, Integer> scores = new HashMap<String, Integer>();
	private final int PTS_KILL, PTS_SUICIDE;
	
	public BombermanGameSession gameSession = null;
}
