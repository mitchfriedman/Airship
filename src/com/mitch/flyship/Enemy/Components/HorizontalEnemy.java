package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.framework.Graphics;
import com.mitch.framework.containers.Vector2d;

/**
 * Created by KleptoKat on 7/20/2014.
 */
public class HorizontalEnemy extends EnemyComponent {

    double speed;
    boolean directionLeft = false;
    boolean includingSpawnFromTop = true;

    public HorizontalEnemy() {}

    public HorizontalEnemy(XmlResourceParser xrp) //xml stuff here
    {
        this();
        speed = Double.valueOf(xrp.getAttributeValue(null, "speed"));
        directionLeft = xrp.getAttributeBooleanValue(null, "directionLeft", false);
        includingSpawnFromTop = xrp.getAttributeBooleanValue(null, "includingSpawnFromTop", true);
    }

    @Override
    public void onComponentAdded() {
        super.onComponentAdded();
        enemy.setVelocity( new Vector2d(directionLeft ? -speed : speed, 0) );
    }

    @Override
    public void onObjectCreationCompletion() {
        super.onObjectCreationCompletion();

        Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
        double x = directionLeft ? g.getWidth() : -enemy.getSize().x;
        double yModifier = includingSpawnFromTop ? Math.abs(g.getWidth()/enemy.getVelocity().x) : 0;
        double y = Math.random() * (g.getHeight()+yModifier) - enemy.getSize().y - yModifier;

        enemy.setPos(new Vector2d(x, y));
    }

    @Override
    public void onUpdate(double deltaTime) {
        super.onUpdate(deltaTime);

        Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
        if (    (enemy.getPos().x > g.getWidth() && !directionLeft) ||
                (enemy.getPos().x < -enemy.getSize().x && directionLeft) ) {
            enemy.getLevel().getBodyManager().removeBody(enemy);
        }
    }

    @Override
    public EnemyComponent clone() {
        HorizontalEnemy enemy = new HorizontalEnemy();

        enemy.directionLeft = directionLeft;
        enemy.speed = speed;
        enemy.includingSpawnFromTop = includingSpawnFromTop;

        return enemy;
    }
}
