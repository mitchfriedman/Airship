package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.framework.containers.Rect;

/**
 * A blank Enemy Component.
 */
public class HitboxModifier extends EnemyComponent {
	
	Rect difference;
	
    public HitboxModifier() {}

    // DEBUGGING: Did you remember to clone the elements?
    public HitboxModifier(XmlResourceParser parser) //xml stuff here
    { 
    	this();
    	difference = new Rect(0,0,0,0);
    	difference.y = parser.getAttributeIntValue(null, "top", 0);
    	difference.x = parser.getAttributeIntValue(null, "left", 0);
    	difference.height = parser.getAttributeIntValue(null, "bottom", 0);
    	difference.width = parser.getAttributeIntValue(null, "right", 0);
    }
    
    @Override
    public void onComponentAdded() {
    	super.onComponentAdded();
    	enemy.setCollisionOffset(difference);
    }
    
    @Override
    public EnemyComponent clone() {
        HitboxModifier component = new HitboxModifier();
        component.difference = difference;
        return component;
    }
}
