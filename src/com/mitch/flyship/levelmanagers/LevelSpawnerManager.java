package com.mitch.flyship.levelmanagers;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.BodySpawner;
import com.mitch.flyship.screens.Level;

public class LevelSpawnerManager {
	final Level level;
	boolean useLevelSpeed = false;
	
	List<BodySpawner> spawners = new ArrayList<BodySpawner>();
	
	public LevelSpawnerManager(Level level, boolean useLevelSpeed) {
		this.level = level;
		this.useLevelSpeed = useLevelSpeed;
	}
	
	public boolean addSpawner(BodySpawner spawner)
	{
		if (spawner.getName() == null || getSpawnerByName(spawner.getName()) == null) {
			spawners.add(spawner);
			return true;
		}
		
		return false;
	}
	
	public boolean spawnerExists(BodySpawner spawner)
	{
		return getSpawnerByName(spawner.getName()) != null;
	}
	
	public BodySpawner getSpawnerByName(String name)
	{
		for (BodySpawner spawner : spawners) {
			if (spawner.getName() == name) {
				return spawner;
			}
		}
		
		return null;
	}
	
	public List<BodySpawner> getSpawners()
	{
		return spawners;
	}
}
