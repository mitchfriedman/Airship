package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Enemy;
import com.mitch.framework.containers.Vector2d;

/**
 * A blank Enemy Component.
 */
class CannonNode extends EnemyComponent {
	
    public CannonNode() { mustBeUnique = false; }
    
    private Vector2d position;
    private boolean directionLeft = true;
    private double interval;
    private double timeSinceLastFire = 0;
    
    // DEBUGGING: Did you remember to clone the elements?
    public CannonNode(XmlResourceParser parser) //xml stuff here
    { 
    	this();
        interval = Double.valueOf(parser.getAttributeValue(null, "interval"));
        directionLeft = parser.getAttributeBooleanValue(null, "directionLeft", true);
        double x = Double.valueOf(parser.getAttributeValue(null, "x"));
        double y = Double.valueOf(parser.getAttributeValue(null, "y"));
        position = new Vector2d(x,y);
    }
    
    @Override
    public void onUpdate(double deltaTime) {
    	super.onUpdate(deltaTime);
    	timeSinceLastFire += deltaTime;
    	
    	if (timeSinceLastFire > interval) {
    		timeSinceLastFire = 0;
    		fireCannon();
    	}
    }
    
    private void fireCannon()
    {
    	Enemy cannonBall = new Enemy(enemy.getLevel(), "CANNON BALL");
    	cannonBall.setDamage(1);
    	cannonBall.setDepth(enemy.getDepth());
    	cannonBall.addComponent(new StaticImage("Enemy/cannon", false, false));
    	cannonBall.addComponent(new HorizontalEnemy(60, directionLeft));
    	
    	for (EnemyComponent comp : cannonBall.getComponents()) {
    		comp.onObjectCreationCompletion();
    	}
    	cannonBall.setPos(enemy.getPos().add(position));
    	
    	enemy.getLevel().getBodyManager().addBodyDuringUpdate(cannonBall);
    }

    @Override
    public EnemyComponent clone() {
        CannonNode component = new CannonNode();
        component.directionLeft = directionLeft;
        component.interval = interval;
        component.position = position;
        
        return component;
    }
}
