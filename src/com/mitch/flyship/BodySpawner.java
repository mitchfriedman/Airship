package com.mitch.flyship;

import java.lang.reflect.Method;
import java.util.List;

import com.mitch.flyship.screens.Level;
import com.mitch.framework.containers.MathHelper;

public class BodySpawner {

	public enum Special {
		NONE,
		RESTART_WITH_HEIGHT
	};
	
    public String getName() {
        return name;
    }
    
    public Special special = Special.NONE;
    private String[] specialSpawnInfo = new String[1]; // We use an array so we can pass the value by reference
    
    private String name;
	private Method spawnObjectsMethod;

    private int startDistance;
    private int spawnRange_Start;
    private int spawnRange_End;
	
    private boolean spawned = false;
    
	private double distanceSinceLastSpawn = 0;
	private double randomSpawnDistance = 0;

    public BodySpawner(Class<? extends GameBody> cls, String name, int spawnRange_Start, int spawnRange_End)
    {
        this(cls, name, 0, spawnRange_Start, spawnRange_End);
    }

    public BodySpawner(Class<? extends GameBody> cls, String name, List<Integer> spawnInfo)
    {
        this(cls, name, spawnInfo.get(0), spawnInfo.get(1), spawnInfo.get(2));
    }

	public BodySpawner(Class<? extends GameBody> cls, String name,
				int startDistance, int spawnRange_Start, int spawnRange_End)
	{
		this.name = name;

        this.startDistance = startDistance;
        this.spawnRange_Start = spawnRange_Start;
        this.spawnRange_End = spawnRange_End;
		
		try {
			// The default special for any object is Special.NONE. Objects that 
			// override the method getSpecial can change this. If an object is special
			// we can also ask for another parameter. If that object doesn't have the
			// capabilities of that special it will result in an error. 
			// Since an array can be passed by reference and we can still maintain
			// the values, we can use it as an "out" parameter. Currently the
			// capacity of this out parameter is the size of the array as
			// declared above. This can be changed to whatever is desired.
			Method getSpecialMethod = cls.getMethod("getSpecial", (Class[]) null);
			special = (Special) getSpecialMethod.invoke(null, (Object[]) null);
			if (special == Special.RESTART_WITH_HEIGHT) {
				this.spawnObjectsMethod = cls.getMethod("spawnObjects", Level.class, String.class, String[].class);
			} else {
				this.spawnObjectsMethod = cls.getMethod("spawnObjects", Level.class, String.class);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateDistance(double distance)
	{
		distanceSinceLastSpawn += distance;
	}
	
	public boolean canSpawn()
	{
		return distanceSinceLastSpawn > spawnRange_Start + randomSpawnDistance + 
				(!spawned ? startDistance : 0);
	}

	@SuppressWarnings("unchecked")
	public List<GameBody> trySpawnObjects(Level level)
	{
		if (spawnObjectsMethod == null) {
			return null;
		}
		
		try {
			if (special == Special.RESTART_WITH_HEIGHT) {
				return (List<GameBody>) spawnObjectsMethod.invoke(null, level, name, specialSpawnInfo);
			} else {
				return (List<GameBody>) spawnObjectsMethod.invoke(null, level, name);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void reset() {
		distanceSinceLastSpawn = 0;
		randomSpawnDistance = Math.random() * (spawnRange_End - spawnRange_Start);

		if (special.equals(Special.RESTART_WITH_HEIGHT) && specialSpawnInfo[0].length() > 0) {
			spawned = false;
			startDistance = Integer.valueOf(specialSpawnInfo[0]);
		} else {
			spawned = true;
		}
		
	}

	/***
	 * Generates a random number from an array of weights. This
	 * function is used locally for selecting a coin configuration.
	 * @param weights An ArrayList of numbers. These numbers are the
	 *                probability of its index being returned
	 * @return Random number in the range of 0 to weights.size()-1
	 *         (inclusive). This number is based on the weights given. -1 on error
	 * @deprecated Use {@link MathHelper#generateRandomValueFromWeights(List<Float>)} instead
	 */
	public static int generateRandomValueFromWeights(List<Float> weights)
	{
		return MathHelper.generateRandomValueFromWeights(weights);
	}

	
	
}
