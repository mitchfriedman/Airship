package com.mitch.framework;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.GameBody;
import com.mitch.flyship.Ship;
import com.mitch.flyship.objects.Enemy;

public class LevelBodyManager {
	
	Ship ship;
	final List<Enemy> enemies;
	final List<GameBody> items;
	
	public LevelBodyManager()
	{
		this.ship = null;
		this.enemies = new ArrayList<Enemy>();
		this.items = new ArrayList<GameBody>();
	}
	
	public void onUpdate(float deltaTime)
	{
		ship.onUpdate(deltaTime);
		for (Enemy enemy : enemies) {
			enemy.onUpdate(deltaTime);
		}
		for (GameBody body : items) {
			body.onUpdate(deltaTime);
		}
	}
	
	public void onPaint(float deltaTime)
	{
		ship.onPaint(deltaTime);
		for (Enemy enemy : enemies) {
			enemy.onPaint(deltaTime);
		}
		for (GameBody body : items) {
			body.onPaint(deltaTime);
		}
	}
	
	
	public void setShip(Ship ship)
	{
		this.ship = ship;
	}
	
	public List<Enemy> getEnemies()
	{
		return enemies;
	}
	
	public List<GameBody> getItems()
	{
		return items;
	}
	
	public void addBodyToEnemies(Enemy body)
	{
		enemies.add(body);
	}
	
	public void addBodyToItems(GameBody body)
	{
		items.add(body);
	}
	
	public void removeBodyFromEnemies(Enemy body)
	{
		enemies.remove(body);
	}
	
	public void removeBodyFromItems(GameBody body)
	{
		items.remove(body);
	}
	
}
