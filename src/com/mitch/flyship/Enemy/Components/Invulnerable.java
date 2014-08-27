package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Ship;

/**
 * A blank Enemy Component.
 */
public class Invulnerable extends EnemyComponent {

    public Invulnerable() {}

    // DEBUGGING: Did you remember to clone the elements?
    public Invulnerable(XmlResourceParser parser) //xml stuff here
    { this(); }

    @Override
    public void onComponentAdded() {
    	super.onComponentAdded();
    	enemy.setDestroyingOnHit(false);
    }
    
    @Override
    public void onHit(Ship ship) {
    	super.onHit(ship);
    	ship.flash();
    	enemy.setColliding(false);
    }

    @Override
    public EnemyComponent clone() {
        Invulnerable component = new Invulnerable();

        return component;
    }
}
