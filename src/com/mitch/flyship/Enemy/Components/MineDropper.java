package com.mitch.flyship.Enemy.Components;

import java.util.Random;

import com.mitch.flyship.Assets;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;
import com.mitch.flyship.Enemy.Components.VerticalEnemy;

import android.content.res.XmlResourceParser;
import android.util.Log;

public class MineDropper extends EnemyComponent {

	private static final float LOWER_DROP_BOUNDS_PERCENT = 0.2f;
	private static final float UPPER_DROP_BOUNDS_PERCENT = 0.8f;
	
	private float xDropPos;
	private float yDropPos;
	private Random random;
	private Image bomber;
	private boolean hasDropped = false;
	
	public MineDropper() {}
	
	public MineDropper(final XmlResourceParser parser) {
		this();
		String imageID = parser.getAttributeValue(null, "ship");
		bomber = Assets.getImage("Enemy/saphire_bomber");
		
	}
	
	@Override
	public void onComponentAdded() {
		super.onComponentAdded();
		random = new Random();
	}
	
	@Override
	public void onObjectCreationCompletion() {
		super.onObjectCreationCompletion();
		
		float screenWidth = enemy.getLevel().getAirshipGame().getGraphics().getWidth();
		float minXPos = screenWidth * LOWER_DROP_BOUNDS_PERCENT;
		float maxXPos = screenWidth * UPPER_DROP_BOUNDS_PERCENT;
		
		xDropPos = random.nextFloat() * screenWidth;
		while(xDropPos <= minXPos || xDropPos >= maxXPos) {
			xDropPos = random.nextFloat() * screenWidth;
		}
		Log.d("xDropPos: ", ""+xDropPos);
	}
	
	@Override
	public void onUpdate(final double deltaTime) {
		super.onUpdate(deltaTime);
		Log.d("MineDropper", "bomber:"+ bomber.toString());
		//handle updating the ship image after it has been dropped off
		
		
		if(!hasDropped && (enemy.getVelocity().x < 0 && enemy.getPos().x < xDropPos) ||
				   enemy.getVelocity().x > 0 && enemy.getPos().x > xDropPos) {
			hasDropped = true;
			
			yDropPos = (float) enemy.getPos().y;
			enemy.removeComponent(VerticalEnemy.class);
			enemy.addComponent(enemy.getComponent(HorizontalEnemy.class).clone());
		} else if(hasDropped) {
			xDropPos += enemy.getVelocity().x;
		}
	}
	
	@Override
	public void onPaint(float deltaTime) {
		super.onPaint(deltaTime);
		//render the mine seperately
		Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
		if(!hasDropped) {
			if(enemy.getVelocity().x < 0) {
				g.drawImage(bomber, new Vector2d(enemy.getPos().x - bomber.getWidth(), enemy.getPos().y));
			} else {
				g.drawImage(bomber, new Vector2d(enemy.getPos().x + bomber.getWidth(), enemy.getPos().y));
			}
			
		} else {
			g.drawImage(bomber, new Vector2d(xDropPos, yDropPos));
		}
		Log.d("hasDropped", ""+hasDropped);
	}

	@Override
	public EnemyComponent clone() {
		MineDropper enemy = new MineDropper();
		
		return enemy;
	}
}
