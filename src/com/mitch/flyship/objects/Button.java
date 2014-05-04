package com.mitch.flyship.objects;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.mitch.flyship.GameBody;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.containers.Vector2d;

public class Button extends GameBody {
	boolean lastTouched = false;
	String text;
	
	public Button(Game game, Vector2d position, String text)
	{
		super(game, position);
		this.text = text;
	}
	
	@Override
	public void onUpdate(float deltaTime) 
	{
		boolean touched = isTouched();
		if (touched && !lastTouched) {
			onDown();
			lastTouched = true;
		}
		else if (lastTouched && !touched) {
			onUp();
			lastTouched = false;
		}
	}
	
	@Override
	public void onPaint(float deltaTime) 
	{
		Paint paint = new Paint();
		paint.setTextSize(36);
		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.WHITE);
		
		Graphics g = game.getGraphics();
		Vector2d size = g.drawString(text, (int) getPos().x, (int) getPos().y, paint);
		size.y = 36;
		setSize(size);
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
