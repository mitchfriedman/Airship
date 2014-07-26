package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * A blank Enemy Component.
 */
public class StaticCollider extends EnemyComponent {

    public StaticCollider() {}

    // DEBUGGING: Did you remember to clone the elements?
    public StaticCollider(XmlResourceParser parser) //xml stuff here
    { this(); }

    @Override
    public void onComponentAdded() {
        super.onComponentAdded();
        enemy.setDestroyingOnHit(false);
    }

    double calculateOverlap(double x, double xWidth, double y, double yWidth)
    {
        double xBegin = x < y ? x : y;
        double xEnd = x+xWidth > y+yWidth ?
                x+xWidth : y+yWidth;
        return xWidth + yWidth - (xEnd - xBegin);
    }

    Vector2d getCenterCoords(Rect rect)
    {
        return rect.getRealPosition().add(rect.getRealSize().scale(0.5));
    }

    @Override
    public void onHit(Ship ship) {
        super.onHit(ship);

        Rect shipBounds = ship.getCollisionBounds();
        Rect enemyBounds = enemy.getCollisionBounds();
        Vector2d shipCenter = getCenterCoords(shipBounds);
        Vector2d enemyCenter = getCenterCoords(enemyBounds);

        Vector2d overlaps = new Vector2d(0,0);

        double enemyHalfWidth = enemyBounds.width/2;
        double shipHalfWidth  = shipBounds.width/2;
        double boxCenterDistance = Math.abs(shipCenter.x - enemyCenter.x);
        double overlapLength = enemyHalfWidth + shipHalfWidth - boxCenterDistance;
        overlaps.x = overlapLength * (shipCenter.x > enemyCenter.x ? -1 : 1);

        enemyHalfWidth = enemyBounds.height/2;
        shipHalfWidth  = shipBounds.height/2;
        boxCenterDistance = Math.abs(shipCenter.y - enemyCenter.y);
        overlapLength = enemyHalfWidth + shipHalfWidth - boxCenterDistance;
        overlaps.y = overlapLength * (shipCenter.y > enemyCenter.y ? -1 : 1);
        Log.d("overlaps", overlaps.x + " " + overlaps.y);


        Vector2d shipModifier;
        if (Math.abs(overlaps.y) < Math.abs(overlaps.x)) {
            shipModifier = new Vector2d(0, -overlaps.y);
        } else {
            shipModifier = new Vector2d(-overlaps.x, 0);
        }
        // do calculations here

        // apparently i suck because I can't really get this working
        // who would've known finding a projection vector was so hardd??

        ship.setPos(shipBounds.getRealPosition().add(shipModifier));
    }

    @Override
    public EnemyComponent clone() {
        StaticCollider component = new StaticCollider();

        return component;
    }
}
