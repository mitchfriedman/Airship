package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mitch.flyship.AnimationImage;
import com.mitch.flyship.Assets;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.Graphics;
import com.mitch.framework.containers.Frame;
import com.mitch.framework.containers.Vector2d;

public class Animation extends EnemyComponent {

	private int numberOfFrames;
	private String rootImage;
	
	private AnimateOn animateOn;
	
	private AnimationImage animation;
	private Vector2d animationPosition;
	
	/* my little animation children, i grant you life
	 * and later on, i shall taketh it away.
	 * I am the god of animation. */
	private enum AnimateOn {
		BIRTH,
		DEATH
	}
	
	public Animation() {
		numberOfFrames = 0;
		rootImage = "";
		animation = new AnimationImage(4);
	}
	
	public Animation(final XmlResourceParser parser) {
		this();
		numberOfFrames = Integer.parseInt(parser.getAttributeValue(null, "numberOfFrames"));
		rootImage = parser.getAttributeValue(null, "rootImage");
		animateOn = AnimateOn.valueOf(parser.getAttributeValue(null, "animateOn"));
	}
	
	@Override
	public void onComponentAdded() {
		super.onComponentAdded();
		
		if(animateOn.equals(AnimateOn.DEATH)) {
			enemy.setDestroyingOnHit(false);
		}
		
		for(int i=1; i<numberOfFrames+1; i++) {
			Log.d("Animation", ""+rootImage+i);
			animation.addFrame(new Frame(Assets.getImage(rootImage+i)));
		}
		
		animation.pause();
		
	}
	
	@Override
	public void onObjectCreationCompletion() {
		super.onObjectCreationCompletion();
				
		if(animateOn.equals(AnimateOn.BIRTH)) {
			animation.resume();
			animationPosition = enemy.getPos()
   					.add(enemy.getSize().divide(2))
   					.subtract(animation.getFrame().image.getSize().divide(2));
		}
	}
	
	@Override
	public void onUpdate(final double deltaTime) {
		super.onUpdate(deltaTime);
		
		if(!animation.isPaused()) {
			animation.updateTime(deltaTime);
			setAnimationPosition();
		}

		if(animateOn.equals(AnimateOn.DEATH) && animation.getCurrentFrameIndex() == animation.getAnimationSize() -1) {
			enemy.getLevel().getBodyManager().removeBody(enemy);
		}
	}
	
	private void setAnimationPosition() {
		animationPosition = enemy.getPos()
		   		.add(enemy.getSize().divide(2))
		   		.subtract(animation.getFrame().image.getSize().divide(2));
	}
	
	@Override
	public void onPaint(final float deltaTime) {
		super.onPaint(deltaTime);
		
		if(!animation.isPaused()) {
			if(animateOn.equals(AnimateOn.DEATH)) {
				Log.d("Animation", "Current frame: "+animation.getCurrentFrameIndex());
			}

			Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
			g.drawImage(animation.getFrame().image, animationPosition);
		}
	}

	@Override
	public void onHit(final Ship ship) {
		super.onHit(ship);
		
		
		if(animateOn.equals(AnimateOn.DEATH) && animation.isPaused()) {
			Log.d("Animation", "hit frame: "+ animation.getCurrentFrameIndex());
			animation.resetAnimation();
			setAnimationPosition();
			animation.resume();
			
			enemy.setColliding(false);
			StaticImage component = enemy.getComponent(StaticImage.class);
			component.setDrawing(false);
		}
	}

	@Override
	public EnemyComponent clone() {
		Animation enemy = new Animation();
		enemy.numberOfFrames = numberOfFrames;
		enemy.rootImage = rootImage;
		enemy.animation = animation;
		enemy.animateOn = animateOn;
		enemy.animationPosition = animationPosition;
		
		return enemy;
	}
}
