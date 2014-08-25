package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.framework.Graphics;
import com.mitch.framework.containers.Vector2d;

/**
 * Created by KleptoKat on 7/20/2014.
 */
public class HorizontalEnemy extends EnemyComponent {

    double speed;
    boolean directionLeft = false;
    boolean randomDirection = false;
    boolean includingSpawnFromTop = true;
    float spawningPositionStart = 0;
    float spawningPositionEnd = 1;
    
    public HorizontalEnemy() {}
    
    public HorizontalEnemy(double speed, boolean directionLeft) 
    {
    	this.speed = speed;
    	this.directionLeft = directionLeft;
    }

    public HorizontalEnemy(XmlResourceParser xrp) //xml stuff here
    {
        speed = Double.valueOf(xrp.getAttributeValue(null, "speed"));
        directionLeft = xrp.getAttributeBooleanValue(null, "directionLeft", false);
        includingSpawnFromTop = xrp.getAttributeBooleanValue(null, "includingSpawnFromTop", true);
        randomDirection = xrp.getAttributeBooleanValue(null, "randomDirection", false);
        
        String sps = xrp.getAttributeValue(null, "spawningPositionStart");
        if (sps != null) {
            spawningPositionStart = Float.valueOf(sps);
        }
        
        String spe = xrp.getAttributeValue(null, "spawningPositionEnd");
        if (spe != null) {
            spawningPositionEnd = Float.valueOf(spe);
        }
    }

    @Override
    public void onComponentAdded() {
        super.onComponentAdded();
        enemy.setVelocity( new Vector2d(directionLeft ? -speed : speed, 0) );
        directionLeft = randomDirection ? Math.floor(Math.random() * 2) == 0 : directionLeft;
    }

    @Override
    public void onObjectCreationCompletion() {
        super.onObjectCreationCompletion();

        Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
        
        double yModifier = includingSpawnFromTop ? Math.abs(g.getWidth()/enemy.getVelocity().x) : 0;
        double spawningPositionStartFinal = g.getHeight() * spawningPositionStart - yModifier;
        double spawningPositionEndFinal = (g.getHeight() - enemy.getSize().y + yModifier) * spawningPositionEnd;
        double y = Math.random() * spawningPositionEndFinal - spawningPositionStartFinal;
        double x = directionLeft ? g.getWidth() : -enemy.getSize().x;
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
        enemy.spawningPositionStart = spawningPositionStart;
        enemy.spawningPositionEnd = spawningPositionEnd;

        return enemy;
    }
}
