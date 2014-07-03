package com.mitch.flyship;

import java.lang.reflect.Method;
import java.util.List;

import com.mitch.flyship.screens.Level;

public class ObjectSpawner {
	
	
	Level level;
	Method spawnObjects;
	
	String name;
	Class<?> cls;
	double spawnStart;
	double spawnEnd;
	
	double distanceSinceLastSpawn = 0;
	double randomSpawnDistance = 0;
	
	public ObjectSpawner(String name, Class<?> cls, double spawnStart, double spawnEnd)
	{
		this(cls, spawnStart, spawnEnd);
		this.name = name;
	}
	
	public ObjectSpawner(Class<?> cls, double spawnStart, double spawnEnd)
	{
		this.name = null;
		this.cls = cls;
		this.spawnStart = spawnStart;
		this.spawnEnd = spawnEnd;
		resetDistanceInfo();
		
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
	
	public double getSpawnStart()
	{
		return spawnStart;
	}
	
	public double getSpawnEnd()
	{
		return spawnEnd;
	}
	
	public void setSpawnDistances(float spawnStart, float spawnEnd)
	{
		this.spawnStart = spawnStart;
		this.spawnEnd = spawnEnd;
	}
	
	public void setLevel(Level level)
	{
		this.level = level;
	}
	
	public double getDistanceSinceLastSpawn()
	{
		return distanceSinceLastSpawn;
	}
	
	public void updateDistance(double distance)
	{
		distanceSinceLastSpawn += distance;
	}
	
	public void resetDistanceInfo()
	{
		distanceSinceLastSpawn = 0;
		randomSpawnDistance = (int) (Math.random() * (spawnEnd - spawnStart));
	}
	
	public boolean canSpawn()
	{
		double speedModifier = level != null ? level.getSpeed() : 1;
		return distanceSinceLastSpawn > spawnStart/speedModifier + randomSpawnDistance/speedModifier;
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
	
	/***
	 * Generates a random number from an array of weights
	 * @param weights An ArrayList of numbers. These numbers are the probability of its index being returned
	 * @return Random number in the range of 0 to weights.size()-1 (inclusive). This number is based on the weights given. -1 on error
	 */
	public static int generateRandomValueFromWeights(List<Float> weights)
	{
		// adds all configuration weights, gets a random number within range of weights.size
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
