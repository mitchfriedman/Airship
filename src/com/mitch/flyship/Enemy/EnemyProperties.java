package com.mitch.flyship.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KleptoKat on 7/20/2014.
 */
public class EnemyProperties {

    private String name;
    private int damage;
    private List<EnemyComponent> components;
    private List<Integer> spawnRange;


    public EnemyProperties(String name, int damage)
    {
        this.name = name;
        this.damage = damage;
        this.components = new ArrayList<EnemyComponent>();
        this.spawnRange = new ArrayList<Integer>(6);
        for (int i=0;i<6;i++) {
            spawnRange.add(0);
        }
    }

    public String getName()
    {
        return name;
    }

    public int getDamage()
    {
        return damage;
    }

    public List<EnemyComponent> getComponents()
    {
        return components;
    }

    public void addTemplateComponent(EnemyComponent component)
    {
        components.add(component);
    }

    public void setLevelSpawnRange(int startLevel, int endLevel)
    {
        spawnRange.set(0, startLevel);
        spawnRange.set(1, endLevel);
    }

    public void setStartSpawnTime(int startRange, int endRange)
    {
        spawnRange.set(2, startRange);
        spawnRange.set(3, endRange);
    }

    public void setEndSpawnTime(int startRange, int endRange)
    {
        spawnRange.set(4, startRange);
        spawnRange.set(5, endRange);
    }

    /**
     * Sets the spawning information
     * @param spawnRange the spawn range in this format:
     *                   Level Spawn Range(0,1)
     *                   startSpawnTimeRange(2,3)
     *                   endSpawnTimeRange(4,5)
     */
    public void setSpawnRange(List<Integer> spawnRange)
    {
        this.spawnRange = spawnRange;
    }

    /**
     * Returns the spawning information
     * @return the spawn range in this format:
     *         Level Spawn Range
     *         startSpawnTimeRange
     *         endSpawnTimeRange
     */
    public List<Integer> getSpawnRange()
    {
        return spawnRange;
    }





}
