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
    boolean directionLeft = true;
    boolean randomDirection = false;
    
    double topOffset = 0;
    boolean topOffsetIsPercent = false;
    double bottomOffset = 0;
    boolean bottomOffsetIsPercent = false;
    
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
        randomDirection = xrp.getAttributeBooleanValue(null, "randomDirection", false);
        
        String sps = xrp.getAttributeValue(null, "topOffset");
        if (sps != null && sps.substring(sps.length()-1).equals("%")) {
        	topOffsetIsPercent = true;
        	topOffset = Float.valueOf(sps.substring(0, sps.length()-1)) / 100f;
        } else if (sps != null) {
        	topOffset = Float.valueOf(sps);
        }
        
        String spe = xrp.getAttributeValue(null, "bottomOffset");
        if (spe != null && spe.substring(spe.length()-1).equals("%")) {
        	bottomOffsetIsPercent = true;
        	bottomOffset = Float.valueOf(spe.substring(0, spe.length()-1)) / 100f;
        } else if (spe != null) {
        	bottomOffset = Float.valueOf(spe);
        }
        
    }

    @Override
    public void onComponentAdded() {
        super.onComponentAdded();
        directionLeft = randomDirection ? Math.floor(Math.random() * 2) == 0 : directionLeft;
        enemy.setVelocity( new Vector2d(directionLeft ? -speed : speed, 0) );
    }

    @Override
    public void onObjectCreationCompletion() {
        super.onObjectCreationCompletion();
        
        Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
        double yStartOffset = topOffset * (double) (topOffsetIsPercent ? g.getHeight() : 1.0);
        double yEndOffset = bottomOffset * (double) (bottomOffsetIsPercent ? g.getHeight() : 1.0);
        
        double yStart = yStartOffset;
        double yEnd = g.getHeight() + yEndOffset;

        double y = Math.random() * (yEnd - yStart) + yStart;
        double x = directionLeft ? g.getWidth() : -enemy.getSize().x;
        enemy.setPos(new Vector2d(x, y));
        
        if (!directionLeft) {
        	StaticImage staticImage = enemy.getComponent(StaticImage.class);
        	staticImage.invertHorizontal = !staticImage.invertHorizontal;
        }
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
        enemy.topOffset = topOffset;
        enemy.bottomOffset = bottomOffset;
        enemy.topOffsetIsPercent = topOffsetIsPercent;
        enemy.bottomOffsetIsPercent = bottomOffsetIsPercent;

        return enemy;
    }
}
