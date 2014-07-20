package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Enemy.EnemyComponent;

/**
 * A blank Enemy Component.
 */
class BlankComponent extends EnemyComponent {

    public BlankComponent() {}

    // DEBUGGING: Did you remember to clone the elements?
    public BlankComponent(XmlResourceParser parser) //xml stuff here
    { this(); }



    @Override
    public EnemyComponent clone() {
        BlankComponent component = new BlankComponent();

        return component;
    }
}
