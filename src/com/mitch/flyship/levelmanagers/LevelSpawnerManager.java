package com.mitch.flyship.levelmanagers;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.ObjectSpawner;
import com.mitch.flyship.screens.Level;

public class LevelSpawnerManager {
	final Level level;
	boolean useLevelSpeed = false;
	
	List<ObjectSpawner> spawners = new ArrayList<ObjectSpawner>();
	
	public LevelSpawnerManager(Level level, boolean useLevelSpeed) {
		this.level = level;
		this.useLevelSpeed = useLevelSpeed;
	}
	
	public boolean addSpawner(ObjectSpawner spawner)
	{
		if (spawner.getName() == null || getSpawnerByName(spawner.getName()) == null) {
			if (useLevelSpeed) {
				spawner.setLevel(level);
			}
			
			spawners.add(spawner);
			return true;
		}
		
		return false;
	}
	
	public boolean spawnerExists(ObjectSpawner spawner)
	{
		return getSpawnerByName(spawner.getName()) != null;
	}
	
	public ObjectSpawner getSpawnerByName(String name)
	{
		for (ObjectSpawner spawner : spawners) {
			if (spawner.getName() == name) {
				return spawner;
			}
		}
		
		return null;
	}
	
	public List<ObjectSpawner> getSpawners()
	{
		return spawners;
	}
}
