package com.mitch.flyship.Enemy;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.objects.Ship;

/**
 * Created by KleptoKat on 7/20/2014.
 */
public abstract class EnemyComponent {

    protected Enemy enemy;

    public EnemyComponent() {}
    public void setEnemy(Enemy enemy)
    {
        this.enemy = enemy;
    }

    public abstract EnemyComponent clone();
    public void onComponentAdded() {}
    public void onObjectCreationCompletion() {}
    public void onPaint(float deltaTime) {}
    public void onUpdate(double deltaTime) {}
    public void onHit(Ship ship) {}

}