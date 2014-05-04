package com.mitch.flyship.objects;

import android.graphics.Paint.Align;
import android.util.Log;

import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Button extends GameBody {
	boolean lastTouched = false;
	boolean down = false;
	Image image;
	Align align = Align.LEFT;
	Vector2d offset = Vector2d.ZERO;
	
	public Button(Game game, Vector2d position, String imageID, int height, Align align)
	{
		super(game, position);
		image = Assets.getImage(imageID);
		setSize(image.getSize().scaleX(height));
		
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
		g.drawImage(image, getPos().x+offset.x, getPos().y+offset.y, getSize().x, getSize().y);
	}
	
	void onDown()
	{
		Log.d("BUTTON", "UP");
		
	}
	
	void onUp()
	{
		Log.d("BUTTON", "DOWN");
		
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}

}
