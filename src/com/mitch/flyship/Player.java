package com.mitch.flyship;

import com.mitch.framework.Input;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Player {
	
	final int MAX_HEALTH = 100;
	final int MAX_WATER = 100;
	
	final int MAX_TILT_UP = 4;
	final int MAX_TILT_DOWN = 4;
	final int MAX_TILT_HORIZONTAL = 4;
	final int MAX_SPEED = 3;
	
	AndroidGame game;
	int health;
	int water;
	int depositedGears;
	int collectedGears;
	
	
	public Player(AndroidGame game)
	{
		this.game = game;
		health = MAX_HEALTH;
		water = MAX_WATER;
		depositedGears = 0;
		collectedGears = 0;
	}
	
	public Vector2d getInput_Speed()
	{
		Input input = game.getInput();
		Vector2d orientation = Vector2d.ZERO;
		orientation = new Vector2d(input.GetTiltX(), input.GetTiltY());
		// TODO: Implement speed / relative orientation
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
		return false;
	}
	
}
