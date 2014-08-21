package com.mitch.flyship;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mitch.flyship.Enemy.Components.VerticalEnemy;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.Enemy.EnemyProperties;
import com.mitch.flyship.screens.Level;

@SuppressWarnings(value = { "unused" })
public class LevelProperties {

    public static List<LevelProperties> levels;

    public static LevelProperties getLevel(int id)
    {
        return levels.get(id);
    }

    public static LevelProperties getLevel(String name)
    {
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getName().equals(name)) {
                return getLevel(i);
            }
        }
        return null;
    }

    public static void loadLevels(XmlResourceParser xrp)
    {
        while(true)
        {
            try
            {
                int eventType = xrp.next();
                if (eventType == XmlResourceParser.END_DOCUMENT) {
                    break;
                }
                if (eventType != XmlResourceParser.START_TAG) {
                    continue;
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.d("XmlPullParserException", e.getLocalizedMessage());
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("IOException", e.getLocalizedMessage());
                continue;
            }


            String tagName = xrp.getName();

            if (tagName.equals( "levels" )) {
                levels = new ArrayList<LevelProperties>();
                Log.d("LevelProperties", "Generating level list...");
            }
            else if (tagName.equals( "level" )) {
                LevelProperties levelProperties = new LevelProperties();

                //General Settings
                levelProperties.name = xrp.getAttributeValue(null, "name");
                levelProperties.background = xrp.getAttributeValue(null, "background");
                levelProperties.music = xrp.getAttributeValue(null, "music");
                levelProperties.ship = xrp.getAttributeValue(null, "ship");
                levelProperties.leaderboardID = xrp.getAttributeValue(null, "leaderboard_id");

                // Level Speed / Max Level (for water, etc.)
                levelProperties.maxLevel = Integer.valueOf(xrp.getAttributeValue(null, "maxLevel"));
                levelProperties.timeToMaxLevel = Double.valueOf(xrp.getAttributeValue(null, "timeToMaxLevel"));
                levelProperties.startSpeed = Double.valueOf(xrp.getAttributeValue(null, "speedAtStart"));
                levelProperties.endSpeed = Double.valueOf(xrp.getAttributeValue(null, "speedAtEnd"));

                // Water
                levelProperties.waterSpawnTimeStart = Integer.valueOf(xrp.getAttributeValue(null, "waterSpawn_RangeStart"));
                levelProperties.waterSpawnTimeEnd = Integer.valueOf(xrp.getAttributeValue(null, "waterSpawn_RangeEnd"));

                levels.add(levelProperties);
                Log.d("LevelProperties", "Loaded Level: " + levelProperties.getName());
            }
            else if (tagName.equals( "enemy" )) {
                loadEnemyFromXML(xrp, levels.get(levels.size()-1));
            }
        }

        Log.d("LevelProperties", levels.size() + " level(s) loaded.");
    }

    private static void loadEnemyFromXML(XmlResourceParser xrp, LevelProperties levelProperties)
    {
        String name = xrp.getAttributeValue(null, "name");
        int damage = Integer.valueOf(xrp.getAttributeValue(null, "damage"));
        EnemyProperties enemyProperties = new EnemyProperties(name, damage);

        if (name == null) {
            return;
        }

        for (EnemyProperties template : levelProperties.getEnemyTemplates()) {
            if (template.getName().equals( enemyProperties.getName() )) {
                return;
            }
        }

        List<Integer> spawnRange = new ArrayList<Integer>(6);
        spawnRange.add(Integer.valueOf(xrp.getAttributeValue(null, "startLevel")));
        spawnRange.add(Integer.valueOf(xrp.getAttributeValue(null, "endLevel")));
        spawnRange.add(Integer.valueOf(xrp.getAttributeValue(null, "startSpawnTime_RangeStart")));
        spawnRange.add(Integer.valueOf(xrp.getAttributeValue(null, "startSpawnTime_RangeEnd")));
        spawnRange.add(Integer.valueOf(xrp.getAttributeValue(null, "endSpawnTime_RangeStart")));
        spawnRange.add(Integer.valueOf(xrp.getAttributeValue(null, "endSpawnTime_RangeEnd")));
        enemyProperties.setSpawnRange(spawnRange);

        while (true) {
            try {
                int eventType = xrp.next();

                if (eventType == XmlResourceParser.END_TAG && xrp.getName().equals("enemy")) {
                    break;
                }

                if (eventType != XmlResourceParser.START_TAG) {
                    continue;
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.d("XmlPullParserException", e.getLocalizedMessage());
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("IOException", e.getLocalizedMessage());
                continue;
            }

            String tagName = xrp.getName();

            try {
                Class<?> componentClass = null;
                Constructor<?> constructor = null;

                componentClass = Class.forName("com.mitch.flyship.Enemy.Components." + tagName);
                constructor = componentClass.getConstructor(XmlResourceParser.class);
                enemyProperties.addTemplateComponent( (EnemyComponent)constructor.newInstance(xrp) );

            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                Log.d("LevelPropertiesLoader", tagName + " does not exist.");
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
                Log.d("LevelPropertiesLoader", tagName + " is not a component. Does it have " +
                      "an XmlResourceParser constructor?");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                Log.d("LevelPropertiesLoader", tagName + " could not be created.");
            } catch (InstantiationException e) {
                e.printStackTrace();
                Log.d("LevelPropertiesLoader", tagName + " could not be created.");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.d("LevelPropertiesLoader", tagName + " could not be created.");
            }
        }

        levelProperties.addEnemyTemplate(enemyProperties);
    }

    public List<EnemyProperties> getEnemyTemplates()
    {
        return enemyTemplates;
    }

    public String getName() {
        return name;
    }

    public String getBackground() {
        return background;
    }

    public String getMusic() {
        return music;
    }

    public String getShip() {
        return ship;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public double getTimeToMaxLevel() {
        return timeToMaxLevel;
    }

    public double getStartSpeed() {
        return startSpeed;
    }

    public double getEndSpeed() {
        return endSpeed;
    }

    public int getWaterSpawnTimeStart() {
        return waterSpawnTimeStart;
    }

    public int getWaterSpawnTimeEnd() {
        return waterSpawnTimeEnd;
    }
    
    public String getLeaderboardID()
    {
    	return leaderboardID;
    }


    private String name, leaderboardID;
	private String background, music, ship;
	private int maxLevel;
    private double timeToMaxLevel;
	private double startSpeed, endSpeed;
	private int waterSpawnTimeStart, waterSpawnTimeEnd;
    private List<EnemyProperties> enemyTemplates = new ArrayList<EnemyProperties>();

	public LevelProperties() {}

    private void addEnemyTemplate(EnemyProperties template)
    {
        enemyTemplates.add(template);
    }
}
