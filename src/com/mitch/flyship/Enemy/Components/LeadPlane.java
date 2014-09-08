package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Enemy;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;

/**
 * A blank Enemy Component.
 */
public class LeadPlane extends EnemyComponent {
	
	String planeLeft, planeRight;
	Align.Vertical align;
	Vector2d planeEnemyOffset = new Vector2d(0,0);
	
    public LeadPlane() {}
    
    // DEBUGGING: Did you remember to clone the elements?
    public LeadPlane(XmlResourceParser parser) //xml stuff here
    { 
    	this(); 
    	planeEnemyOffset.x = parser.getAttributeIntValue(null, "xOffset", 0);
    	planeEnemyOffset.y = parser.getAttributeIntValue(null, "yOffset", 0);
    	planeLeft = parser.getAttributeValue(null, "planeLeft");
    	planeRight = parser.getAttributeValue(null, "planeRight");
    	align = Align.Vertical.valueOf(parser.getAttributeValue(null, "align"));
    }
    
    @Override
    public void onObjectCreationCompletion() {
    	super.onObjectCreationCompletion();
    	boolean directionLeft = enemy.getVelocity().x < 0;
    	
    	
    	Enemy plane = new Enemy(enemy.getLevel(), "LEAD PLANE: " + enemy.getName());
    	plane.addComponent(new StaticImage(directionLeft ? planeLeft : planeRight, false, false));
    	plane.addComponent(new HorizontalEnemy(enemy.getVelocity().x, directionLeft));
    	//plane.addComponent(new PositionLogger());

    	for (EnemyComponent comp : plane.getComponents()) {
    		comp.onObjectCreationCompletion();
    	}
    	
    	double yOffset = 0;
    	switch(align) {
    	case BOTTOM:
    		yOffset = enemy.getSize().y - plane.getSize().y;
    		break;
    	case CENTER:
    		yOffset = enemy.getSize().y/2 - plane.getSize().y/2;
    		break;
    	case TOP:
    	default:
    		break;
    	}
    	
    	plane.setPos(enemy.getPos().add(new Vector2d(0, yOffset)));
    	Log.d("PLANE POS", plane.getPos().x + ", " + plane.getPos().y);
    	enemy.getLevel().getBodyManager().addBodyDuringUpdate(plane);
    	
    	// Modifies enemy position for lead to be in front of the enemy.
    	enemy.setPos(enemy.getPos().add(new Vector2d(directionLeft ? planeEnemyOffset.x + plane.getSize().x 
    			: -planeEnemyOffset.x - enemy.getSize().x, -planeEnemyOffset.y)));
    	

    	Log.d("leadplane", "ZEPPELIN SPAWNED AT: " + enemy.getPos().y);
    }
    


    @Override
    public EnemyComponent clone() {
        LeadPlane component = new LeadPlane();
        component.align = align;
        component.planeEnemyOffset = planeEnemyOffset;
        component.planeLeft = planeLeft;
        component.planeRight = planeRight;
        
        return component;
    }
}
