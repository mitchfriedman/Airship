package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;
import com.mitch.flyship.Enemy.EnemyComponent;

public class TouchDestroyable extends EnemyComponent {

	private int health;

	private boolean lastTouched = false;
	boolean down = false;
	
	public TouchDestroyable() {}
	
	public TouchDestroyable(final XmlResourceParser parser) {
		this();
		health = Integer.parseInt(parser.getAttributeValue(null, "health"));
	}
	
	@Override
	public void onUpdate(final double deltaTime) {		
		boolean touched = enemy.isTouched(enemy.getOffset());
		
		if(lastTouched && !touched) {
			if(health <= 0) {
				enemy.onHit(enemy.getLevel().getBodyManager().getShip());
			} else {
				health--;
			}	
		}
		lastTouched = touched;
	}
	
	@Override
	public EnemyComponent clone() {
		TouchDestroyable enemy = new TouchDestroyable();
		enemy.health = health;
		return enemy;
	}
	
	
}
