package com.mitch.flyship;

import com.mitch.framework.Input;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Player {
	
	final int WATER_VALUE = 30;
	final int HEALTH_VALUE = 30;
	
	final int MAX_HEALTH = 90;
	final int MAX_WATER = 90;
	
	final int MAX_TILT_UP = 1;
	final int MAX_TILT_DOWN = 7;
	final int MAX_TILT_HORIZONTAL = 4;
	final int MAX_SPEED = 3;
	
	AndroidGame game;
	boolean paused;
	boolean lights;
	int health;
	int water;
	int depositedGears;
	int collectedGears; // value might not be needed. We'll keep it for now.
	
	
	public Player(AndroidGame game)
	{
		this.game = game;
		health = MAX_HEALTH;
		water = MAX_WATER;
		paused = false;
		lights = false;
		depositedGears = 0; 
	}
	
	public Vector2d getInput_Speed()
	{
		Input input = game.getInput();
		Vector2d orientation = Vector2d.ZERO;
		orientation = new Vector2d(input.GetTiltX(), input.GetTiltY());
		
		if (orientation.x > MAX_TILT_HORIZONTAL) {
			orientation.x = MAX_TILT_HORIZONTAL;
		}
		else if (orientation.x < -MAX_TILT_HORIZONTAL) {
			orientation.x = -MAX_TILT_HORIZONTAL;
		}
		
		if (orientation.y > MAX_TILT_DOWN) {
			orientation.y = MAX_TILT_DOWN;
		}
		
		if (orientation.y < -MAX_TILT_UP) {
			orientation.y = -MAX_TILT_UP;
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
