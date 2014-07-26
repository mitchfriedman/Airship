package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;

public class Button extends GameBody {
	boolean lastTouched = false;
	boolean lastScreenTouched = false;
	boolean wasCanceled = false;
	boolean down = false;
	Align align;
	Vector2d offset = new Vector2d(0,0);
	ButtonClickListener listener;

	Image pressedImage;
	Image activeImage;
	Image currentImage;
	
	
	public Button(AirshipGame game, String name, Align align, Vector2d pos, ButtonClickListener listener)
	{
		super(game, name, pos);
		this.listener = listener;
		
		activeImage  = Assets.getImage(name+"-active");
		pressedImage = Assets.getImage(name+"-hover");
		currentImage = activeImage;
		setSize(currentImage.getSize());
		
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
	public void onUpdate(double deltaTime)
	{
		boolean touched = isTouched(offset);
		boolean screenTouched = game.getInput().isTouchDown(0);
		
		if (touched && !lastTouched && (!lastScreenTouched || wasCanceled)) {
			listener.onDown();
			onDown();
			
			down = true;
			lastTouched = true;
		}
		else if (lastTouched && !touched && screenTouched) {
			listener.onCancel();
			onCancel();
			
			wasCanceled = true;
			down = false;
			lastTouched = false;
		}
		else if (lastTouched && !touched) {
			listener.onUp();
			onUp();
			
			down = false;
			lastTouched = false;
		}
		
		if (!screenTouched && lastScreenTouched) {
			lastScreenTouched = false;
			wasCanceled = false;
		}
		else if (screenTouched && !lastScreenTouched) {
			lastScreenTouched = true;
		}
	}
	
	@Override
	public void onPaint(float deltaTime) 
	{
		Graphics g = game.getGraphics();
		g.drawImage(currentImage, getPos().add(offset));
	}
	
	void onDown()
	{
		currentImage = pressedImage;
	}
	
	void onUp()
	{
		currentImage = activeImage;
	}
	
	void onCancel()
	{
		onUp();
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}

}
