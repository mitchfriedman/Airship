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

    private AnimationImage explosion;

    public ExplosionAnimation() {}

    // DEBUGGING: Did you remember to clone the elements?
    public ExplosionAnimation(XmlResourceParser parser) //xml stuff here
    { this(); }

    @Override
    public void onComponentAdded() {
        super.onComponentAdded();

        enemy.setDepth(500);
        enemy.setDestroyingOnHit(false);
        explosion = new AnimationImage(8);
        explosion.addFrame(new Frame(Assets.getImage("Explosion/original/1")));
        explosion.addFrame(new Frame(Assets.getImage("Explosion/original/2")));
        explosion.addFrame(new Frame(Assets.getImage("Explosion/original/3")));
        explosion.addFrame(new Frame(Assets.getImage("Explosion/original/4")));
        explosion.addFrame(new Frame(Assets.getImage("Explosion/original/5")));
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

        enemy.setColliding(false);
        enemy.setSize(new Vector2d(0,0));

        StaticImage comp = enemy.getComponent(StaticImage.class);
        comp.setDrawing(false);
    }

    @Override
    public void onUpdate(double deltaTime) {
        super.onUpdate(deltaTime);
        if (!explosion.isPaused()) {
            explosion.updateTime(deltaTime);
            enemy.setVelocity(enemy.getVelocity().scale(0.7));
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
            g.drawImage(explosion.getFrame().image, enemy.getPos());
        }
    }

    @Override
    public EnemyComponent clone() {
        ExplosionAnimation component = new ExplosionAnimation();

        return component;
    }
}
