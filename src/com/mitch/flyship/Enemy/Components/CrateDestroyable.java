package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Assets;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

/**
 * A blank Enemy Component.
 */
public class CrateDestroyable extends EnemyComponent {
	
	private static final double START_SPEED = 4.0;
	private static final double END_SPEED = 1.5;
	private static final double DURATION = 0.4;
	private static final int SIZE = 12;
	
	private static final String[] imageLocations = 
		{ "crate-TL", "crate-TR",
		  "crate-BL", "crate-BR" };
	private Image[] images;
	
	private double distance = 0;
	private Vector2d[] positions = new Vector2d[4];
	private boolean activated = false;
	private double timeElapsed = 0.0;
	
	
    public CrateDestroyable() {}

    // DEBUGGING: Did you remember to clone the elements?
    public CrateDestroyable(XmlResourceParser parser) //xml stuff here
    { this(); }
    
    @Override
    public void onComponentAdded() {
    	super.onComponentAdded();
    	enemy.setDestroyingOnHit(false);
    }
    
    @Override
    public void onUpdate(double deltaTime) {
    	super.onUpdate(deltaTime);
    	
    	if (enemy.isTouched(new Vector2d(0,0)) && !activated) {
    		onTouch();
    	}
    	
    	if (activated && timeElapsed < DURATION) {
    		timeElapsed += deltaTime;
    		
    		double speed = Math.abs((END_SPEED - START_SPEED))
        			* (DURATION / timeElapsed)
        			+ START_SPEED;
        	distance += speed * deltaTime;
        	
    		calculateDistance();
    		
    	} else if (timeElapsed >= DURATION) {
    		enemy.getLevel().getBodyManager().removeBody(enemy);
    	}
    }
    
    @Override
    public void onPaint(float deltaTime) {
    	super.onPaint(deltaTime);
    	if (activated) {
    		Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
        	for (int i = 0; i < 4; i++) {
        		g.drawImage(images[i], positions[i]);
        	}
    	}
    	
    }
    
    @Override
    public void onHit(Ship ship) {
    	super.onHit(ship);
    	activate();
    }
    
    public void onTouch()
    {
    	activate();
    }
    
    public void activate()
    {
    	enemy.getLevel().getAirshipGame().getAchievementManager().onCrateBreak();
    	
    	if (!activated) {
    		images = new Image[] {
    				Assets.getImage(imageLocations[0]),
    				Assets.getImage(imageLocations[1]),
    				Assets.getImage(imageLocations[2]),
    				Assets.getImage(imageLocations[3]),
    		};
    		
    		enemy.removeComponent(StaticImage.class);
    		enemy.setVelocity(new Vector2d(0,0));
    		calculateDistance();
        	activated = true;
    	}
    }
    
    public void calculateDistance()
    {
    	double x,y;
    	
    	x = enemy.getPos().x - distance;
    	y = enemy.getPos().y - distance;
    	positions[0] = new Vector2d(x,y);
    	
    	x = enemy.getPos().x + enemy.getSize().x - SIZE + distance;
    	y = enemy.getPos().y - distance;
    	positions[1] = new Vector2d(x,y);
    	
    	x = enemy.getPos().x - distance;
    	y = enemy.getPos().y + enemy.getSize().y - SIZE + distance;
    	positions[2] = new Vector2d(x,y);
    	
    	x = enemy.getPos().x + enemy.getSize().x - SIZE + distance;
    	y = enemy.getPos().y + enemy.getSize().y - SIZE + distance;
    	positions[3] = new Vector2d(x,y);
    }
    
    @Override
    public EnemyComponent clone() {
        CrateDestroyable component = new CrateDestroyable();

        return component;
    }
}
