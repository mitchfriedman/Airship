package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mitch.flyship.Enemy.EnemyComponent;

public class Repair extends EnemyComponent {

	private double timeOnPlatform;
	
	private double timeToRepair;
	private int repairAmount;
	
	public Repair() {
		timeOnPlatform = 0;
	}
	
	public Repair(final XmlResourceParser parser) {
		this();
		timeToRepair = Double.parseDouble(parser.getAttributeValue(null, "timeToRepair"));
		repairAmount = Integer.parseInt(parser.getAttributeValue(null, "repairAmount"));
		//TODO: check for null values here
	}
	
    @Override
    public void onComponentAdded() {
    	super.onComponentAdded();
    	enemy.setDestroyingOnHit(false);
    }
	
	@Override
	public void onUpdate(final double deltaTime) {
		super.onUpdate(deltaTime);

		if(enemy.isCollidingWith(enemy.getLevel().getBodyManager().getShip())) {
			if(timeOnPlatform >= timeToRepair) {
				enemy.getLevel().getBodyManager().getShip().getPlayer().addHealth(repairAmount);
				timeOnPlatform = 0;
			} else {
				timeOnPlatform += deltaTime;
			}
		}
	}
	
	public EnemyComponent clone() {
		Repair enemy = new Repair();
		enemy.timeToRepair = timeToRepair;
		enemy.repairAmount = repairAmount;
		
		return enemy;
	}
}
