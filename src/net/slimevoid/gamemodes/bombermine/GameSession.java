package net.slimevoid.gamemodes.bombermine;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
	
	public GameSession() {
		sessions.add(this);
		this.duration = -1;
		this.name = "MainSession";
	}
	public GameSession(int duration) {
		this();
		this.duration = duration;
	}
	
	public GameSession(String name, int duration) {
		this();
		this.duration = duration;
		this.name = name;
	}
	
	public static List<GameSession> getSessionsList(){
		return sessions;
	}
	
	public void onTick() {
		if (currentTick == 0) {
			System.out.println("Session pre-starting : "+name);
			onPreStart();
		}
		if (currentTick == preStart) {
			if(canStart()) {
				if(isWainting && restartOnStart) {
					isWainting = false;
					restart();
					return;
				} else {
					System.out.println("Session Started : "+name);
					isStarted = true;
					onStart();
				}
			} else {
				isWainting = true;
				return;
			}
		}
		if(duration == -1 && isStarted) {
			currentTick = preStart + 1;
		} else {
			currentTick ++;
		}
		if (currentTick >= duration && duration != -1) {
			onStop();
			if (autoRestart()) {
				restart();
			} else {
				System.out.println("Session End : " + name);
				isStarted = false;
				remove();
			}
		} 
	}
	
	public void onPreStart() {
	}
	
	public void onStart() {
		System.out.println("Session start : " + name);
	}
	
	public void onStop() {
	}
	
	public boolean canStart() {
		return true;
	}
	
	public void restart() {
		System.out.println("Session Restart : " + name);
		isStarted = false;
		currentTick = 0;
	}
	
	public void remove(){
		sessions.remove(this);
	}
	
	public boolean autoRestart() {
		return false;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	public int duration;
	public int currentTick = 0;
	public int preStart = 0;
	public String name;
	private final static List<GameSession> sessions = new ArrayList<GameSession>();
	protected boolean isStarted = false, isWainting = false, restartOnStart = true;
}
