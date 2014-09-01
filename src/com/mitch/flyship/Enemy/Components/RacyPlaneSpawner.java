package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Assets;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Enemy;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

/**
 * A blank Enemy Component.
 */
public class RacyPlaneSpawner extends EnemyComponent {
	
	private static final int SQUAD_WIDTH = 30;
	private static final int SQUAD_HEIGHT = 10;
	private static final int SQUAD_PLANE_COUNT = 25;
	private static final int INCOMING_PLANE_LEAD_DISTANCE = 100;
	private static final String racyPath = "racyplane";
	
	private Image racyplane;
	
    public RacyPlaneSpawner() {}

    // DEBUGGING: Did you remember to clone the elements?
    public RacyPlaneSpawner(XmlResourceParser parser) //xml stuff here
    { this(); }

    @Override
    public void onComponentAdded() {
    	super.onComponentAdded();
    	racyplane = Assets.getImage(racyPath);
    	
    	Vector2d squadSize_px = new Vector2d(SQUAD_WIDTH * racyplane.getWidth(), 
    			SQUAD_HEIGHT * racyplane.getHeight());
    	
    	// calculates the offset of the entire squad relative incoming plane
		Vector2d squadOffset = new Vector2d(
				squadSize_px.x + INCOMING_PLANE_LEAD_DISTANCE,
				-squadSize_px.y);
    	
		boolean[][] squadMap = generateMap();
    	for (int x = 0; x < SQUAD_WIDTH; x++) {
    		for (int y = 0; y < SQUAD_HEIGHT; y++) {
    			if (squadMap[x][y]) {
    				
    				// Gets the offset of the plane relative to the squad
    				Vector2d planePosition = new Vector2d(x * racyplane.getWidth(),
    						y * racyplane.getHeight());
    				// Calculates the position of the enemy on the screen
        			Vector2d pos = enemy.getPos().add(squadOffset).add(planePosition);
        			
        			spawnPlane(pos);
    			}
    		}
    	}
    }
    
    public boolean[][] generateMap()
    {
    	boolean[][] squadMap = new boolean[SQUAD_WIDTH][SQUAD_HEIGHT];
    	for (int i = 0; i < SQUAD_PLANE_COUNT; i++) {
    		int x = (int) (Math.random() * SQUAD_WIDTH);
    		int y = (int) (Math.random() * SQUAD_HEIGHT);
    		squadMap[x][y] = true;
    	}
    	return squadMap;
    }
    
    public void spawnPlane(Vector2d position)
    {
    	Enemy plane = new Enemy(enemy.getLevel(), "PLANE");
    	plane.setDepth(enemy.getDepth());
    	plane.setDamage(1);
    	plane.addComponent(new StaticImage(racyPath, false, false));
    	plane.addComponent(new ExplosionAnimation());
    	plane.addComponent(new HorizontalEnemy( 0, true ));
    	
    	for (EnemyComponent comp : plane.getComponents()) {
    		comp.onObjectCreationCompletion();
    	}
    	
    	plane.setVelocity(enemy.getVelocity().scale(Math.random() * 0.4 + 0.7));
    	plane.setPos(position);
    	enemy.getLevel().getBodyManager().addBodyDuringUpdate(plane);
    }
    
    @Override
    public EnemyComponent clone() {
        RacyPlaneSpawner component = new RacyPlaneSpawner();

        return component;
    }
}
