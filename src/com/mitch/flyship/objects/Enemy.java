package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.objects.enemytypes.VerticalEnemy;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.containers.Vector2d;

public abstract class Enemy extends GameBody {
	
	protected Level level;
	int damage = 0;

	public Enemy(Level level) {
		super(level.getAirshipGame(), "Enemy");
		this.level = level;
	}
	
	
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public abstract void onHit();

	@Override
	public void onUpdate(float deltaTime) {
		setPos(getPos().add(velocity));
	}

	@Override
	public void onPaint(float deltaTime) { }

	@Override
	public void onPause() { }

	@Override
	public void onResume() { }
	
	public static List<GameBody> spawnObjects(Level level, String type)
	{
		Graphics g = level.getAirshipGame().getGraphics();
		
		if (type == "BIRD") {
			List<GameBody> birdList = new ArrayList<GameBody>();
			
			VerticalEnemy birds = new VerticalEnemy(level, Assets.getImage("Enemy/flock_of_gulls"), 1);
			double xPos = Math.random() * (g.getWidth()-birds.getSize().x);
			birds.setPos(new Vector2d(xPos, -birds.getSize().y));
			
			birdList.add(birds);
			return birdList;
		}
		
		return null;
	}
	
}
