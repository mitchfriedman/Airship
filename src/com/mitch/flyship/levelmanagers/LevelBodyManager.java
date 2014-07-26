package com.mitch.flyship.levelmanagers;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.GameBody;
import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.containers.Vector2d;

public class LevelBodyManager {
	
	Ship ship;
	final List<GameBody> bodies = new ArrayList<GameBody>();
	final List<GameBody> removeQueue = new ArrayList<GameBody>();
	
	public LevelBodyManager()
	{
		this.ship = null;
	}
	
	public void onUpdate(double deltaTime)
	{
		removeBodies();
		for (GameBody body : bodies) {
			body.onUpdate(deltaTime);
		}
	}
	
	public void onPaint(float deltaTime)
	{
		removeBodies();
		for (GameBody body : bodies) {
			body.onPaint(deltaTime);
		}
	}
	
	
	public void setShip(Ship ship)
	{
		bodies.remove(this.ship);
		this.ship = ship;
		addBody(ship);
	}
	
	public Ship getShip()
	{
		return ship;
	}
	
	public List<GameBody> getBodies()
	{
		return bodies;
	}
	
	public List<Enemy> getEnemies()
	{
		return getBodiesByClass(Enemy.class);
	}
	
	public void addBody(GameBody body)
	{
		for (int i = 0; i < bodies.size(); i++) {
			if (bodies.get(i).getDepth() > body.getDepth()) {
				bodies.add(i, body);
				return;
			}
		}
		bodies.add(body);
	}
	
	
	public void offsetBodies(Vector2d offset)
	{
        //ship.setPos(ship.getPos().add(offset));
		for (GameBody body : bodies) {
			if (body.affectedByLevelSpeed) {
				body.setPos(body.getPos().add(offset));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public<T extends GameBody> List<T> getBodiesBySuperClass(Class<T> type)
	{
		
		List<T> itemsOfType = new ArrayList<T>();
		for (GameBody body : bodies) {
			Class<?> cls = body.getClass().getSuperclass();
			if (cls == type) {
				itemsOfType.add((T)body);
			}
		}
		return itemsOfType;
	}
	
	@SuppressWarnings("unchecked")
	public<T extends GameBody> List<T> getBodiesByClass(Class<T> type)
	{
		
		List<T> itemsOfType = new ArrayList<T>();
		for (GameBody body : bodies) {
			Class<?> cls = body.getClass();
			if (cls == type) {
				itemsOfType.add((T)body);
			}
		}
		return itemsOfType;
	}
	
	public void removeBody(GameBody body)
	{
		removeQueue.add(body);
	}
	
	void removeBodies()
	{
		for (GameBody body : removeQueue) {
			bodies.remove(body);
		}
		removeQueue.clear();
	}
}
