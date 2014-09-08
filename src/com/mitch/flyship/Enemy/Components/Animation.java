package com.mitch.flyship.Enemy.Components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

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

	private final static String FRAME = "Frame";
	
	private List<String> images;
	private List<Boolean> reverseX;
	private List<Boolean> reverseY;
	
	private int fps;
	
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
		fps = 0;
		images = new ArrayList<String>();
		reverseX = new ArrayList<Boolean>();
		reverseY = new ArrayList<Boolean>();
	}
	
	public Animation(final XmlResourceParser parser) {
		this();
		fps = Integer.parseInt(parser.getAttributeValue(null, "fps"));
		animateOn = AnimateOn.valueOf(parser.getAttributeValue(null, "animateOn"));
		//adding somethign to be parsed that is a part of the Animation XML node
		//must be put above this function call to parseImages, as it alters the xml
		parseImages(parser);
		animation = new AnimationImage(fps);
		for(int i=0;i<images.size();i++) {
			animation.addFrame(new Frame(Assets.getImage(images.get(i)), reverseX.get(i), reverseY.get(i)));
		}
	}
	
	@Override
	public void onComponentAdded() {
		super.onComponentAdded();
		
		enemy.setSize(animation.getFrame(0).image.getSize());
		if(animateOn.equals(AnimateOn.DEATH)) {
			enemy.setDestroyingOnHit(false);
		}
	}
	
	@Override
	public void onObjectCreationCompletion() {
		super.onObjectCreationCompletion();
				
		if(animateOn.equals(AnimateOn.BIRTH)) {
			animation.resume();
			setAnimationPosition();
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
	
	@Override
	public void onPaint(final float deltaTime) {
		super.onPaint(deltaTime);
		
		if(!animation.isPaused()) {

			Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
			g.drawImage(animation.getFrame().image, animationPosition);
		}
	}

	@Override
	public void onHit(final Ship ship) {
		super.onHit(ship);
		
		/* not working right now, this can only be used for BIRTH animations */
		
		/*if(animateOn.equals(AnimateOn.DEATH) && animation.isPaused()) {
			animation.resetAnimation();
			setAnimationPosition();
			
			enemy.setColliding(false);
			StaticImage component = enemy.getComponent(StaticImage.class);
			component.setDrawing(false);
		}*/
	}

	@Override
	public EnemyComponent clone() {
		Animation enemy = new Animation();
		enemy.fps = fps;
		enemy.animation = animation;
		enemy.animateOn = animateOn;
		enemy.animationPosition = animationPosition;
		
		return enemy;
	}
	
	private void parseImages(final XmlResourceParser parser) {
		while(true) {
			try {
                int eventType = parser.next();
                if (eventType == XmlResourceParser.END_TAG && parser.getName().equals("Animation")) {
                    break;
                }
                
                if (eventType != XmlResourceParser.START_TAG) {
                    continue;
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.d("XmlPullParserException", e.getLocalizedMessage());
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("IOException", e.getLocalizedMessage());
                continue;
            }
			
			String tagName = parser.getName();
			
			if(tagName.equals(FRAME)) {
				String image = parser.getAttributeValue(null, "image");
				images.add(image);
				reverseX.add(Boolean.parseBoolean(parser.getAttributeValue(null, "reverseX")));
				reverseY.add(Boolean.parseBoolean(parser.getAttributeValue(null, "reverseY")));
			}
		}
	}
	
	private void setAnimationPosition() {
		animationPosition = enemy.getPos()
		   		.add(enemy.getSize().divide(2))
		   		.subtract(animation.getFrame().image.getSize().divide(2));
	}
}
