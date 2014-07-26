package com.mitch.flyship;

import java.util.List;

import com.mitch.flyship.screens.Level;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public abstract class GameBody {
	public boolean affectedByLevelSpeed = true;
	protected AirshipGame game;
	protected Vector2d velocity;
	protected Vector2d offset = new Vector2d(0,0);
	protected Rect collisionOffset = new Rect(0,0,0,0);
	float depth = 100;
    Rect bounds;
	String name;

	public GameBody(AirshipGame game, String name)
	{
		this.game = game;
		this.name = name;
		this.velocity = new Vector2d(0,0);
		this.bounds = new Rect(0,0,0,0);
	}
	
	public GameBody(AirshipGame game, String name, Rect bounds)
	{
		this(game, name);
		this.bounds = bounds;
	}
	
	public GameBody(AirshipGame game, String name, Vector2d position)
	{
		this(game, name);
		this.bounds = new Rect(position, new Vector2d(0,0));
	}
	
	public void setDepth(float depth)
	{
		this.depth = depth;
	}
	
	public float getDepth()
	{
		return depth;
	}
	
	public void setBounds(Rect bounds) 
	{
		this.bounds = bounds;
	}
	
	public Rect getBounds() 
	{
		return bounds;
	}
	
	public Rect getCollisionBounds()
	{
		Rect rect = new Rect(bounds);
		rect.x += collisionOffset.x;
		rect.y += collisionOffset.y;
		rect.width  += collisionOffset.width  - collisionOffset.x;
		rect.height += collisionOffset.height - collisionOffset.y;
		return rect;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setPos(Vector2d position)
	{
		bounds.setPosition(position);
		onSetPos();
	}
	
	protected void onSetPos() {}
	
	public Vector2d getPos()
	{
		return bounds.getRealPosition();
	}
	
	public Vector2d getOffset()
	{
		return offset;
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
    public void setVelocity(Vector2d velocity) { this.velocity = velocity; }
	
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
	
	public abstract void onUpdate(double deltaTime);
	public abstract void onPaint(float deltaTime);
	public void onPause() {}
	public void onResume() {}
	
	
	public static List<GameBody> spawnObjects(Level level, String type)
	{
		return null;
	}
	
}