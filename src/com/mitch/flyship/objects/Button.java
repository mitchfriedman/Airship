package com.mitch.flyship.objects;

import com.mitch.framework.containers.Align;

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
	Align align;
	Vector2d offset = Vector2d.ZERO;
	ButtonClickListener listener;

	Image pressedImage;
	Image activeImage;
	Image currentImage;
	
	
	public Button(AirshipGame game, String name, Align align, ButtonClickListener listener)
	{
		super(game, name);
		setPos(new Vector2d(0,0));
		
		try {
			activeImage  = Assets.getImage(name+"-active");
			pressedImage = Assets.getImage(name+"-hover");
		}
		catch (Exception e){
			
		}
		
		
		currentImage = activeImage;

		this.listener = listener;
		
		switch(align.getHorizontal()) {
		case LEFT:
			break;
		case CENTER:
			offset.x = -getSize().x/2;
			break;
		case RIGHT:
			offset.x = -getSize().x;
			break;
		}
		switch(align.getVertical()) {
			case TOP:
				break;
			case BOTTOM:
				offset.y = -getSize().y;
				break;
			case CENTER:
				offset.y = -getSize().y/2;
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
		g.drawImage(currentImage, getPos().x+offset.x, getPos().y+offset.y);
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
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}

}
