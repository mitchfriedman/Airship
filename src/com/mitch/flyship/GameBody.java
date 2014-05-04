package com.mitch.flyship;

import com.mitch.framework.Game;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public abstract class GameBody {
	protected Game game;
	Rect bounds;
	
	public GameBody(Game game)
	{
		this.game = game;
	}
	
	public GameBody(Game game, Rect bounds)
	{
		this(game);
		this.bounds = bounds;
	}
	
	public GameBody(Game game, Vector2d position)
	{
		this(game);
		this.bounds = new Rect(position, Vector2d.ZERO);
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
	
	public boolean isTouched()
	{
		Input input = game.getInput();
		if (input.isTouchDown(0)) {
			Vector2d touchPos = new Vector2d(input.getTouchX(0), input.getTouchY(0));
			return Rect.vectorWithinRect(touchPos, bounds);
		} else {
			return false;
		}
		
	}
	
	public abstract void onUpdate(float deltaTime);
	public abstract void onPaint(float deltaTime);
	public abstract void onPause();
	public abstract void onResume();
	
	
}
