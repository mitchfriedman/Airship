package com.mitch.framework;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.GameBody;
import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.containers.Vector2d;

public class LevelBodyManager {
	
	Ship ship;
	final List<Enemy> enemies;
	final List<GameBody> items;
	final List<GameBody> removeQueue;
	
	public LevelBodyManager()
	{
		this.ship = null;
		this.enemies = new ArrayList<Enemy>();
		this.items = new ArrayList<GameBody>();
		this.removeQueue = new ArrayList<GameBody>();
	}
	
	public void onUpdate(float deltaTime)
	{
		removeBodies();
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
		removeBodies();
		
		for (Enemy enemy : enemies) {
			enemy.onPaint(deltaTime);
		}
		for (GameBody body : items) {
			body.onPaint(deltaTime);
		}
		ship.onPaint(deltaTime);
	}
	
	
	public void setShip(Ship ship)
	{
		this.ship = ship;
	}
	
	public Ship getShip()
	{
		return ship;
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
	
	@SuppressWarnings("unchecked")
	public<T extends GameBody> List<T> getItemsByType(Class<T> type)
	{
		List<T> itemsOfType = new ArrayList<T>();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getClass() == type) {
				itemsOfType.add((T)items.get(i));
			}
		}
		return itemsOfType;
	}
	
	public List<Enemy> getEnemiesByType(Class<Enemy> type)
	{
		//TODO: Implement getEnemiesByType
		return null;
	}
	
	public void removeBody(GameBody body)
	{
		removeQueue.add(body);
	}
	
	public void offsetItems(Vector2d offset)
	{
		for (GameBody body : items) {
			body.setPos(body.getPos().add(offset));
		}
	}
	
	public void offsetEnemies(Vector2d offset)
	{
		for (Enemy body : enemies) {
			body.setPos(body.getPos().add(offset));
		}
	}
	
	void removeBodies()
	{
		for (GameBody body : removeQueue) {
			items.remove(body);
			enemies.remove(body);
		}
	}
}
