package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.AnimationImage;
import com.mitch.flyship.Assets;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.Graphics;
import com.mitch.framework.containers.Frame;
import com.mitch.framework.containers.Vector2d;

/**
 * A blank Enemy Component.
 */
class ExplosionAnimation extends EnemyComponent {
	
	static final int N_EXPLOSIONS = 7;
	
    private AnimationImage explosion;
    private Vector2d offset = new Vector2d(0,0);
    private Vector2d explosionPos = new Vector2d(0,0);

    public ExplosionAnimation() {}

    // DEBUGGING: Did you remember to clone the elements?
    public ExplosionAnimation(XmlResourceParser parser) //xml stuff here
    { 
    	this(); 
    	offset.x = parser.getAttributeFloatValue(null, "x", 0);
    	offset.y = parser.getAttributeFloatValue(null, "y", 0);
    }

    @Override
    public void onComponentAdded() {
        super.onComponentAdded();

        enemy.setDestroyingOnHit(false);
        explosion = new AnimationImage(11);
        for (int i = 1; i <= N_EXPLOSIONS; i++) {
        	explosion.addFrame(new Frame(Assets.getImage("Explosion/original/" + i)));
        }
        
        explosion.pause();
    }

    private void beginAnimation()
    {
        explosion.resume();
    }

    @Override
    public void onHit(Ship ship) {
        super.onHit(ship);
        beginAnimation();

        enemy.setDepth(500);

        enemy.setColliding(false);

        StaticImage comp = enemy.getComponent(StaticImage.class);
        comp.setDrawing(false);
    }

    @Override
    public void onUpdate(double deltaTime) {
        super.onUpdate(deltaTime);
        if (!explosion.isPaused()) {
            explosion.updateTime(deltaTime);
            enemy.setVelocity(enemy.getVelocity().scale(0.7));
            explosionPos = enemy.getPos()
            		.add(enemy.getSize().divide(2)
            		.subtract(explosion.getFrame().image.getSize().divide(2)))
            		.add(offset);
        }

        if (explosion.getCurrentFrameIndex() == explosion.getAnimationSize()-1) {
            enemy.getLevel().getBodyManager().removeBody(enemy);
        }
        
    }

    @Override
    public void onPaint(float deltaTime) {
        super.onPaint(deltaTime);
        if (!explosion.isPaused()) {
            Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
            g.drawImage(explosion.getFrame().image, explosionPos);
        }
    }

    @Override
    public EnemyComponent clone() {
        ExplosionAnimation component = new ExplosionAnimation();
        component.offset = offset;
        return component;
    }
}
