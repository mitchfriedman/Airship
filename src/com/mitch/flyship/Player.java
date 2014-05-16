package com.mitch.flyship;

import com.mitch.framework.Input;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Player {
	
	final int WATER_VALUE = 30;
	final int HEALTH_VALUE = 30;
	
	final int MAX_HEALTH = 90;
	final int MAX_WATER = 90;
	
	final double MAX_TILT_UP = 1;
	final double MAX_TILT_DOWN = 1.5;
	final double MAX_TILT_HORIZONTAL = 1;
	
	final double MAX_SPEED_UP = 1;
	final double MAX_SPEED_DOWN = 1.5;
	final double MAX_SPEED_HORIZONTAL = 0.8;
	
	final double UP_TILT_LIMITER = 0.15;
	final double DOWN_TILT_LIMITER = 0.15;
	final double HORIZONTAL_TILT_LIMITER = 0.1;
	
	AndroidGame game;
	Vector2d orientationOffset = new Vector2d(0, 3.5);
	boolean paused;
	boolean lights;
	int health;
	int water;
	int depositedGears;
	int collectedGears; // value might not be needed. We'll keep it for now.
	
	
	public Player(AirshipGame game)
	{
		this.game = game;
		health = MAX_HEALTH;
		water = MAX_WATER;
		paused = false;
		lights = false;
		depositedGears = 0; 
	}
	
	public void centerOrientation()
	{
		orientationOffset = getOrientation();
	}
	
	public Vector2d getOrientation()
	{
		Input input = game.getInput();
		return new Vector2d(input.GetTiltX(), input.GetTiltY());
	}
	
	public Vector2d getCenteredOrientation()
	{
		return getOrientation().subtract(orientationOffset);
	}
	
	public Vector2d getInput_Speed()
	{
		Vector2d orientation = getCenteredOrientation();
		
		if (orientation.x > MAX_TILT_HORIZONTAL) {
			orientation.x = -MAX_SPEED_HORIZONTAL;
		}
		else if (orientation.x < -MAX_TILT_HORIZONTAL) {
			orientation.x = MAX_SPEED_HORIZONTAL;
		} 
		else if(orientation.x > HORIZONTAL_TILT_LIMITER && orientation.x < -HORIZONTAL_TILT_LIMITER) {
			orientation.x *= MAX_SPEED_HORIZONTAL / MAX_TILT_HORIZONTAL;
		} else {
			orientation.x = 0;
		}
		
		
		if (orientation.y > MAX_TILT_DOWN) {
			orientation.y = MAX_SPEED_DOWN;
		}
		else if (orientation.y > DOWN_TILT_LIMITER) {
			orientation.y *= MAX_SPEED_UP / MAX_TILT_UP;
		}
		
		
		if (orientation.y < -MAX_TILT_UP) {
			orientation.y = -MAX_SPEED_DOWN;
		}
		else if (orientation.y < -UP_TILT_LIMITER) {
			orientation.y *= MAX_SPEED_DOWN / MAX_TILT_DOWN;
		}
		
		if (orientation.y > -UP_TILT_LIMITER && orientation.y < DOWN_TILT_LIMITER) {
			orientation.y = 0;
		}
		
		return orientation;
	}
	
	public boolean getInput_ShootLeft()
	{
		Input input = game.getInput();
		if (input.isTouchDown(0)) {
			return input.getTouchX(0) < game.getGraphics().getWidth()/2;
		} else {
			return false;
		}
	}
	
	public boolean getInput_ShootRight()
	{
		Input input = game.getInput();
		if (input.isTouchDown(0)) {
			return input.getTouchX(0) > game.getGraphics().getWidth()/2;
		} else {
			return false;
		}
	}
	
	public boolean getInput_Lights()
	{
		return lights;
	}
	
	public void addWater()
	{
		water += WATER_VALUE;
	}
	
	public void addHealth()
	{
		health += HEALTH_VALUE;
	}
	
	public void applyDamage(int damage)
	{
		health -= damage;
	}
	
	public void addGears(int amount)
	{
		depositedGears += amount;
	}
	
	public void addGear()
	{
		addGears(1);
	}
	
	public void pause()
	{
		
	}
	
	public void onPaint(float deltaTime)
	{
		// draw GUI here
	}
	
}
