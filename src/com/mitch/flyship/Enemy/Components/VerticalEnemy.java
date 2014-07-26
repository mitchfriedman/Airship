package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.framework.Graphics;
import com.mitch.framework.containers.Vector2d;

/**
 * Created by KleptoKat on 7/20/2014.
 */
public class VerticalEnemy extends EnemyComponent {

    double speed;
    boolean directionDown = true;

    public VerticalEnemy() {}

    public VerticalEnemy(XmlResourceParser xrp) //xml stuff here
    {
        this();
        speed = Double.valueOf(xrp.getAttributeValue(null, "speed"));
        directionDown = !xrp.getAttributeValue(null, "directionUp").equals("true");
    }

    @Override
    public void onComponentAdded() {
        super.onComponentAdded();

        enemy.setVelocity(new Vector2d(0, directionDown ? speed : -speed));
    }

    @Override
    public void onObjectCreationCompletion() {
        super.onObjectCreationCompletion();

        Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
        double x = Math.random() * (g.getWidth() - enemy.getSize().x);
        double y = directionDown ? -enemy.getSize().y : g.getHeight();
        enemy.setPos(new Vector2d(x, y));
    }

    @Override
    public void onUpdate(double deltaTime) {
        super.onUpdate(deltaTime);

        Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
        if (    (enemy.getPos().y > g.getHeight() && directionDown) ||
                (enemy.getPos().y < 0 && !directionDown) ) {
            enemy.getLevel().getBodyManager().removeBody(enemy);
        }
    }

    @Override
    public EnemyComponent clone() {
        VerticalEnemy enemy = new VerticalEnemy();

        enemy.directionDown = directionDown;
        enemy.speed = speed;

        return enemy;
    }
}
