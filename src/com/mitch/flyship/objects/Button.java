package com.mitch.flyship.objects;

import android.graphics.Paint.Align;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Button extends GameBody {
	boolean lastTouched = false;
	boolean down = false;
	Align align = Align.LEFT;
	Vector2d offset = Vector2d.ZERO;
	ButtonClickListener listener;
	
	
	
	//Image depressedImage;
	Image pressedImage;
	Image activeImage;
	Image currentImage;
	
	//boolean justTouched = false;
	//float timeTouchUp = 0;
	/*
	enum state {
		pressed,
		//depressed,
		active
	}
	state CURRENT;*/
	
	public Button(AirshipGame game, String name, Vector2d position, String imageID, int height, Align align, ButtonClickListener listener)
	{
		super(game, name, position);
		//CURRENT = state.active;
		
		
		try {
			activeImage = Assets.getImage(imageID);
			pressedImage = Assets.getImage(imageID+" Pressed");
			
			//depressedImage = Assets.getImage(imageID+" Depressed");
		}
		catch (Exception e){
			
		}
		
		setSize(activeImage.getSize().scaleX(height));
		setSize(pressedImage.getSize().scaleX(height));
		//setSize(depressedImage.getSize().scaleX(height));
		
		currentImage = activeImage;

		this.listener = listener;
		
		switch(align) {
		case LEFT:
			break;
		case CENTER:
			offset.x = -getSize().x/2;
			break;
		case RIGHT:
			offset.x = -getSize().x;
			break;
		}
	}
	public Image getImage() {
		return currentImage;
	}
	/* FUNCTION LIMITATION:
	 *  - function only sets the current image which is useless, 
	 *    need a function for active and pressed images if it is to be used properly
	*/
	public void setImage(String image) {
		
		currentImage = Assets.getImage(image);
	}
	@Override
	public void onUpdate(float deltaTime) 
	{
		boolean touched = isTouched(offset);
		
		/*if(justTouched) {
			if(timeTouchUp < 40 && timeTouchUp > 0) {
				currentImage = depressedImage;
				timeTouchUp += deltaTime;	
			}
			
			else if(timeTouchUp >= 40) {
				currentImage = activeImage;
				timeTouchUp = 0;
				justTouched = false;
			}
		}
		*/
		
		if (touched && !lastTouched) {
			onDown();
			down = true;
			lastTouched = true;
		}
		else if (lastTouched && !touched) {
			onUp();
			
			down = false;
			lastTouched = false;
		}
	}
	
	@Override
	public void onPaint(float deltaTime) 
	{
		Graphics g = game.getGraphics();
		g.drawImage(currentImage, getPos().x+offset.x, getPos().y+offset.y, getSize().x, getSize().y);
	}
	
	void onDown()
	{
		listener.onDown();
		currentImage = pressedImage;
	}
	
	void onUp()
	{
		listener.onUp();
		currentImage = activeImage;
		//justTouched = true;
		//timeTouchUp = 1;
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}

}
