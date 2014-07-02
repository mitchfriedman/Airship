package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.objects.enemytypes.HorizontalEnemy;
import com.mitch.flyship.objects.enemytypes.VerticalEnemy;
import com.mitch.flyship.screens.Level;

public abstract class Enemy extends GameBody {
	
	public static HashMap<String, Enemy> enemies = new HashMap<String, Enemy>();
	
	protected Level level;
	protected int damage = 0;

	public Enemy(Level level) 
	{
		super(level.getAirshipGame(), "Enemy");
		this.level = level;
	}
	
	public abstract boolean withinBounds();
	public abstract void onHit();
	public abstract Enemy spawn();
	
	@Override
	public void onUpdate(float deltaTime)
	{
		setPos(getPos().add(velocity));
		
		if (!withinBounds()) {
			level.getBodyManager().removeBody(this);
		}
	}
	
	@Override
	public void onPaint(float deltaTime) { }

	@Override
	public void onPause() { }

	@Override
	public void onResume() { }
	
	public int getDamage() 
	{
		return damage;
	}
	
	public static void generateDictionary(Level level)
	{
		enemies.put("BIRD", new VerticalEnemy(level, 1, Assets.getImage("Enemy/flock_of_gulls"), 1, true));
		enemies.put("FIGHTER", new VerticalEnemy(level, 1, Assets.getImage("Enemy/crimson_fighter"), 1.2, true));
		enemies.put("GUNSHIP", new HorizontalEnemy(level, 1, Assets.getImage("Enemy/gunship"), 1, true));
	}
	
	public static List<GameBody> spawnObjects(Level level, String type)
	{
		if (enemies.containsKey(type)) {
			
			List<GameBody> enemyList = new ArrayList<GameBody>();
			enemyList.add(enemies.get(type).spawn());
			return enemyList;
		}
		else {
			return null;
		}
	}
}
