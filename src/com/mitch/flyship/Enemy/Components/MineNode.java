package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Assets;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class MineNode extends EnemyComponent {

	private static final float LOWER_DROP_BOUNDS_PERCENT = 0.2f;
	private static final float UPPER_DROP_BOUNDS_PERCENT = 0.8f;
	
	private double xDropPos;
	private boolean hasDropped = false;
	
	private Image mine;
	Vector2d minePosition;
	
	public MineNode() {}
	
	public MineNode(final XmlResourceParser parser) {
		this();
		String imageID = parser.getAttributeValue(null, "mine");
		double x = Double.parseDouble(parser.getAttributeValue(null, "x"));
		double y = Double.parseDouble(parser.getAttributeValue(null, "y"));
		minePosition = new Vector2d(x,y);
		mine = Assets.getImage(imageID);
	}
	
	@Override
	public void onObjectCreationCompletion() {
		super.onObjectCreationCompletion();
		
		float screenWidth = enemy.getLevel().getAirshipGame().getGraphics().getWidth();
		float minXPos = screenWidth * LOWER_DROP_BOUNDS_PERCENT;
		float maxXPos = screenWidth * UPPER_DROP_BOUNDS_PERCENT;
		
		xDropPos = Math.random() * (maxXPos - minXPos) + minXPos;
	}
	
	@Override
	public void onUpdate(final double deltaTime) {
		super.onUpdate(deltaTime);
		//handle updating the ship image after it has been dropped off
		
		boolean readyToDrop = enemy.getVelocity().x < 0 && enemy.getPos().x < xDropPos ||
							  enemy.getVelocity().x > 0 && enemy.getPos().x > xDropPos;
		
		if(!hasDropped && readyToDrop) {
			dropMine();
		}
	}
	
	private void dropMine()
	{
		if (!hasDropped) {
			hasDropped = true;
			
			Enemy mine = new Enemy(enemy.getLevel(), "MINE");
			mine.setDamage(1);
			enemy.setDamage(enemy.getDamage()-1);
			mine.setDepth(enemy.getDepth());
			mine.addComponent(new StaticImage("Enemy/mine", false, false));
			mine.addComponent(new ExplosionAnimation());
	    	
	    	for (EnemyComponent comp : mine.getComponents()) {
	    		comp.onObjectCreationCompletion();
	    	}
	    	mine.setPos(enemy.getPos().add(minePosition));
	    	
	    	enemy.getLevel().getBodyManager().addBodyDuringUpdate(mine);
		}
	}
	
	@Override
	public void onHit(Ship ship) {
		super.onHit(ship);
		
		dropMine();
	}
	
	@Override
	public void onPaint(float deltaTime) {
		super.onPaint(deltaTime);
		
		if (!hasDropped) {
			Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
			g.drawImage(mine, enemy.getPos().add(minePosition));
		}
	}

	@Override
	public EnemyComponent clone() {
		MineNode enemy = new MineNode();
		enemy.mine = mine;
		enemy.minePosition = minePosition;
		return enemy;
	}
}
