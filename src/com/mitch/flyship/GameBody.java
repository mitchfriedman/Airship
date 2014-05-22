package com.mitch.flyship;

import java.util.List;

import com.mitch.flyship.screens.Level;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public abstract class GameBody {
	protected AirshipGame game;
	protected Vector2d velocity;
	protected Vector2d offset;
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
	public void setBounds(Rect bounds) {
		this.bounds = bounds;
	}
	public Rect getBounds() {
		return bounds;
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
	
	public static BodyConfiguration getBodyConfiguration(int configID)
	{
		BodyConfiguration config = new BodyConfiguration();
		config.addConfigurationObject(new Vector2d(0,0), null);
		return config;
	}
	
	public static List<GameBody> getBodiesFromConfiguration(BodyConfiguration config,
			Vector2d pos, Level level) 
	{
		return null;
	}
	
	
}
