package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mitch.flyship.Enemy.EnemyComponent;

/**
 * A blank Enemy Component.
 */
public class PositionLogger extends EnemyComponent {

    public PositionLogger() {}

    // DEBUGGING: Did you remember to clone the elements?
    public PositionLogger(XmlResourceParser parser) //xml stuff here
    { this(); }

    @Override
    public void onUpdate(double deltaTime) {
    	super.onUpdate(deltaTime);
    	Log.d("POSITION OF " + enemy.getName(), enemy.getPos().x + ", " + enemy.getPos().y);
    }
    

    @Override
    public EnemyComponent clone() {
        PositionLogger component = new PositionLogger();

        return component;
    }
}
