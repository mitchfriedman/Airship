package com.mitch.flyship;

import com.mitch.framework.Input;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public abstract class GameBody {
	protected AndroidGame game;
	protected Vector2d velocity;
	Rect bounds;
	String name;
	
	public GameBody(AndroidGame game, String name)
	{
		this.game = game;
		this.name = name;
		this.velocity = Vector2d.ZERO;
		this.bounds = new Rect(0,0,0,0);
	}
	
	public GameBody(AndroidGame game, String name, Rect bounds)
	{
		this(game, name);
		this.bounds = bounds;
	}
	
	public GameBody(AndroidGame game, String name, Vector2d position)
	{
		this(game, name);
		this.bounds = new Rect(position, Vector2d.ZERO);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setPos(Vector2d position)
	{
		bounds.setPosition(position);
	}
	
	public Vector2d getPos()
	{
		return bounds.getRealPosition();
	}
	
	public void setSize(Vector2d size)
	{
		bounds.setSize(size);
	}
	
	public Vector2d getSize()
	{
		return bounds.getRealSize();
	}
	
	public Vector2d getVelocity()
	{
		return velocity;
	}
	
	public boolean isTouched(Vector2d offset)
	{
		Input input = game.getInput();
		if (input.isTouchDown(0)) {
			Vector2d touchPos = new Vector2d(input.getTouchX(0), input.getTouchY(0));
			Rect touchBox = new Rect(bounds);
			touchBox.x += offset.x;
			touchBox.y += offset.y;
			return Rect.vectorWithinRect(touchPos, touchBox);
		} else {
			return false;
		}
	}
	
	public abstract void onUpdate(float deltaTime);
	public abstract void onPaint(float deltaTime);
	public abstract void onPause();
	public abstract void onResume();
	
	
}
