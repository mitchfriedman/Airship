package com.mitch.flyship;

import java.lang.reflect.Method;
import java.util.List;

import com.mitch.flyship.screens.Level;

public class ObjectSpawner {
	
	Method spawnObjects;
	
	public Class<?> cls;
	public float spawnStart;
	public float spawnEnd;
	
	float timeSinceLastSpawn = 0;
	float randomSpawnTime = 0;
	
	public ObjectSpawner(Class<?> cls, float spawnStart, float spawnEnd)
	{
		this.cls = cls;
		this.spawnStart = spawnStart;
		this.spawnEnd = spawnEnd;
		resetTimeInfo();
		
		try {
			this.spawnObjects = cls.getMethod("spawnObjects", Level.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
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
		return timeSinceLastSpawn > spawnStart + randomSpawnTime;
	}
	
	@SuppressWarnings("unchecked")
	public List<GameBody> trySpawnObjects(Level level)
	{
		if (spawnObjects == null) {
			return null;
		}
		
		try {
			return (List<GameBody>) spawnObjects.invoke(null, (Object) level);
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
