package com.mitch.framework;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.GameBody;
import com.mitch.flyship.Ship;

public class LevelBodyManager {
	
	private final Ship ship;
	private final List<GameBody> enemies;
	private final List<GameBody> items;
	
	public LevelBodyManager(Ship ship)
	{
		this.ship = ship;
		this.enemies = new ArrayList<GameBody>();
		this.items = new ArrayList<GameBody>();
	}
	
	public List<GameBody> getEnemies()
	{
		return enemies;
	}
	
	public List<GameBody> getItems()
	{
		return items;
	}
	
	public void addBodyToEnemies(GameBody body)
	{
		enemies.add(body);
	}
	
	public void addBodyToItems(GameBody body)
	{
		items.add(body);
	}
	
	public void removeBodyFromEnemies(GameBody body)
	{
		enemies.remove(body);
	}
	
	public void removeBodyFromItems(GameBody body)
	{
		items.remove(body);
	}
	
	
}
