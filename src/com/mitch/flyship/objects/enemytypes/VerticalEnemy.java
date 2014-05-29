package com.mitch.flyship.objects.enemytypes;

import android.util.Log;

import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class VerticalEnemy extends Enemy {
	
	Image image;
	
	public VerticalEnemy(Level level, Image image, double speed)
	{
		super(level);
		super.velocity = new Vector2d(0,speed);
		this.image = image;
		setSize(image.getSize());
	}
	
	@Override
	public void onPaint(float deltaTime) 
	{
		super.onPaint(deltaTime);
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos());
	}
	
	@Override
	public void onHit() 
	{
		Log.d("VerticalEnemy", "HIT");
		level.getBodyManager().removeBody(this);
	}
}
