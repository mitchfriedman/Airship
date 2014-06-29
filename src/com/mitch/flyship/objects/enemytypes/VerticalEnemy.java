package com.mitch.flyship.objects.enemytypes;

import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class VerticalEnemy extends Enemy {
	
	Image image;
	boolean facingDown;
	
	public VerticalEnemy(Level level, Image image, double speed, boolean facingDown)
	{
		super(level);
		super.velocity = new Vector2d(0,speed);
		this.facingDown = facingDown;
		this.image = image;
		setSize(image.getSize());
	}
	
	@Override
	public void onPaint(float deltaTime) 
	{
		super.onPaint(deltaTime);
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos(), false, (facingDown && velocity.y < 0) || (!facingDown && velocity.y > 0));
	}
	
	@Override
	public Enemy spawn() {
		VerticalEnemy enemy = new VerticalEnemy(level, image, velocity.y, facingDown);
		
		Graphics g = level.getAirshipGame().getGraphics();
		double xPos = Math.random() * ( g.getWidth() - enemy.getSize().x );
		double yPos = velocity.y < 0 ? g.getHeight() : -enemy.getSize().y;
		enemy.setPos(new Vector2d(xPos, yPos));
		
		return enemy;
	}
	
	@Override
	public void onHit() 
	{
		level.getBodyManager().removeBody(this);
	}
	
	@Override
	public boolean withinBounds() {
		int height = level.getAirshipGame().getGraphics().getHeight();
		
		return (velocity.y < 0 && getPos().y > -getSize().y) || (velocity.y > 0 && getPos().y < height);
	}
}
