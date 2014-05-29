package com.mitch.flyship.objects.enemytypes;

import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class HorizontalEnemy extends Enemy {
	
	Image image;
	boolean facingLeft;
	
	public HorizontalEnemy(Level level, Image image, double speed, boolean facingLeft)
	{
		super(level);
		super.velocity = new Vector2d(speed,0);
		this.image = image;
		this.facingLeft = facingLeft;
		setSize(image.getSize());
	}
	
	@Override
	public void onPaint(float deltaTime) 
	{
		super.onPaint(deltaTime);
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos(), !facingLeft && velocity.x < 0, false);
	}
	
	@Override
	public Enemy spawn() {
		HorizontalEnemy enemy = new HorizontalEnemy(level, image, velocity.x, facingLeft);
		
		Graphics g = level.getAirshipGame().getGraphics();
		double xPos = velocity.y < 0 ? g.getWidth() : -enemy.getSize().x;
		double yPos = Math.random() * ( g.getHeight() - enemy.getSize().y );
		enemy.setPos(new Vector2d(xPos, yPos));
		
		return enemy;
	}
	
	@Override
	public void onHit() 
	{
		level.getBodyManager().removeBody(this);
	}
}
