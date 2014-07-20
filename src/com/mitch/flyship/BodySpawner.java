package com.mitch.flyship;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

import com.mitch.flyship.screens.Level;

public class BodySpawner {

    public String getName() {
        return name;
    }

    public int getLevelStart() {
        return levelStart;
    }

    public int getLevelEnd() {
        return levelEnd;
    }

    public boolean isCappedAtMaxLevel() {
        return cappedAtMaxLevel;
    }

    public void setCappedAtMaxLevel(boolean maxLevelRangeCap) {
        this.cappedAtMaxLevel = maxLevelRangeCap;
    }

    public boolean isIgnoringLevelRange() {
        return ignoringLevelRange;
    }

    public void setIgnoringLevelRange(boolean ignoreLevelRange) {
        this.ignoringLevelRange = ignoreLevelRange;
    }

    String name;
	Method spawnObjects;
    Class<? extends GameBody> cls;

    final int levelStart;
    final int levelEnd;
	final double spawnStart_Range1;
    final double spawnStart_Range2;
    final double spawnEnd_Range1;
    final double spawnEnd_Range2;
    double spawnRangeStart;
    double spawnRangeEnd;
    double spawnRangeProgress;

    boolean cappedAtMaxLevel = false;
    boolean ignoringLevelRange = true;
	
	double distanceSinceLastSpawn = 0;
	double randomSpawnDistance = 0;

    public BodySpawner(Class<? extends GameBody> cls, String name, double spawnStart, double spawnStop)
    {
        this(cls, name, 0, 0, spawnStart, spawnStop, spawnStart, spawnStop);
        ignoringLevelRange = true;
    }

    public BodySpawner(Class<? extends GameBody> cls, String name, List<Integer> spawnInfo)
    {
        this(cls, name,
                spawnInfo.get(0), spawnInfo.get(1),
                spawnInfo.get(2), spawnInfo.get(3),
                spawnInfo.get(4), spawnInfo.get(5));
    }

	public BodySpawner(Class<? extends GameBody> cls, String name,
                       int lvl_start, int lvl_end,
                       double start_rng1, double start_rng2,
                       double end_rng1, double end_rng2)
	{
		this.name = name;
		this.cls = cls;

        spawnRangeProgress = 0;
        this.levelStart = lvl_start;
        this.levelEnd = lvl_end;
        this.spawnStart_Range1 = start_rng1;
        this.spawnStart_Range2 = start_rng2;
        this.spawnEnd_Range1 = end_rng1;
        this.spawnEnd_Range2 = end_rng2;

        updateLevel(1);
		resetSpawnDistance();
		
		try {
			this.spawnObjects = cls.getMethod("spawnObjects", Level.class, String.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

    public void updateLevel(int currentLevel)
    {
        if (levelStart < levelEnd && !ignoringLevelRange) {
            double progress = (levelEnd - levelStart) / currentLevel;
            this.spawnRangeProgress = progress > 1 && cappedAtMaxLevel ? 1 : progress;
        }

        spawnRangeStart = spawnStart_Range1 +
                          (spawnEnd_Range1 - spawnStart_Range1) * (spawnRangeProgress+1);
        spawnRangeEnd = spawnStart_Range2 +
                (spawnEnd_Range2 - spawnStart_Range2) * (spawnRangeProgress+1);
    }

	public void updateDistance(double distance)
	{
        distanceSinceLastSpawn += distance;
	}
	
	public void resetSpawnDistance()
	{
		distanceSinceLastSpawn = 0;
		randomSpawnDistance = (int) (Math.random() * (spawnRangeEnd - spawnRangeStart));
	}
	
	public boolean canSpawn(int level, double distanceFactor)
	{
        return //((level >= levelStart && level < levelEnd) || ignoringLevelRange) &&
                (distanceSinceLastSpawn > spawnRangeStart*distanceFactor +
                randomSpawnDistance*distanceFactor);
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
	 * Generates a random number from an array of weights. This
     * function is used locally for selecting a coin configuration.
	 * @param weights An ArrayList of numbers. These numbers are the
     *                probability of its index being returned
	 * @return Random number in the range of 0 to weights.size()-1
     *         (inclusive). This number is based on the weights given. -1 on error
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
