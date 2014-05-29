package com.mitch.flyship;

import java.lang.reflect.Method;
import java.util.List;

import com.mitch.flyship.screens.Level;

public class ObjectSpawner {
	
	
	Level level;
	Method spawnObjects;
	
	String name;
	Class<?> cls;
	float spawnStart;
	float spawnEnd;
	
	float timeSinceLastSpawn = 0;
	float randomSpawnTime = 0;
	
	public ObjectSpawner(String name, Class<?> cls, float spawnStart, float spawnEnd)
	{
		this(cls, spawnStart, spawnEnd);
		this.name = name;
	}
	
	public ObjectSpawner(Class<?> cls, float spawnStart, float spawnEnd)
	{
		this.name = null;
		this.cls = cls;
		this.spawnStart = spawnStart;
		this.spawnEnd = spawnEnd;
		resetTimeInfo();
		
		try {
			this.spawnObjects = cls.getMethod("spawnObjects", Level.class, String.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public Class<?> getSpawnerClass()
	{
		return cls;
	}
	
	public float getSpawnStart()
	{
		return spawnStart;
	}
	
	public float getSpawnEnd()
	{
		return spawnEnd;
	}
	
	public void setSpawnTimes(float spawnStart, float spawnEnd)
	{
		this.spawnStart = spawnStart;
		this.spawnEnd = spawnEnd;
	}
	
	public void setLevel(Level level)
	{
		this.level = level;
	}
	
	public float getTimeSinceLastSpawn()
	{
		return timeSinceLastSpawn;
	}
	
	public void updateTime(float deltaTime)
	{
		timeSinceLastSpawn += deltaTime;
	}
	
	public void resetTimeInfo()
	{
		timeSinceLastSpawn = 0;
		randomSpawnTime = (int) (Math.random() * (spawnEnd - spawnStart));
	}
	
	public boolean canSpawn()
	{
		double speedModifier = level != null ? level.getSpeed() : 1;
		return timeSinceLastSpawn > spawnStart/speedModifier + randomSpawnTime/speedModifier;
	}
	
	@SuppressWarnings("unchecked")
	public List<GameBody> trySpawnObjects(Level level)
	{
		if (spawnObjects == null) {
			return null;
		}
		
		try {
			return (List<GameBody>) spawnObjects.invoke(null, level, name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getRandomValueFromWeights(List<Float> weights)
	{
		// adds all configuration weights, gets a random number within all weights
		// finds corresponding ID.
		float weightTotal = 0;
		for (Float v : weights) {
			weightTotal += v;
		}
		
		float randomWeight = (float) (Math.random() * weightTotal);
		float weightUsed = 0;
		for (int i = 0; i < weights.size(); i++) {
			weightUsed += weights.get(i);
			if (randomWeight < weightUsed) {
				return i;
			}
		}
		// Will never happen ... like that girlfriend ):
		return 0;
	}
	
}
