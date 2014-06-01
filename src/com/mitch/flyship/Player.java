package com.mitch.flyship;

import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Player {
	
	final int WATER_VALUE = 30;
	final int HEALTH_VALUE = 30;
	
	final int MAX_HEALTH = 90;
	final int MAX_WATER = 90;
	
	final double MAX_TILT_UP = 1.5;
	final double MAX_TILT_DOWN = 2.3;
	final double MAX_TILT_HORIZONTAL = 3;
	
	final double MAX_SPEED_UP = 2.2;
	final double MAX_SPEED_DOWN = 0.4;
	public final double MAX_SPEED_HORIZONTAL = 1.4;
	
	final double UP_TILT_LIMITER = 0.2;
	final double DOWN_TILT_LIMITER = 0.2;
	final double HORIZONTAL_TILT_LIMITER = 0.4;
	
	AndroidGame game;
	Vector2d orientationOffset = new Vector2d(0, 3);
	int currency = 0;
	boolean paused = false;
	boolean lights = false;
	int health = MAX_HEALTH;
	int water = MAX_WATER;
	
	Image hud;;
	Image hud1;
	Image hud2;
	
	
	public Player(AirshipGame game)
	{
		this.game = game;
		hud1 = Assets.getImage("hud1");
		hud2 = Assets.getImage("hud2");
		hud = hud1;
	}
	
	public void centerOrientation()
	{
		orientationOffset = getOrientation();
	}
	
	public Vector2d getOrientation()
	{
		Input input = game.getInput();
		return new Vector2d(-input.GetTiltX(), input.GetTiltY());
	}
	
	public Rect getShipBounds()
	{
		Graphics g = game.getGraphics();
		return new Rect(0,0,g.getWidth(),g.getHeight()-hud.getHeight());
	}
	
	public Vector2d getCenteredOrientation()
	{
		return getOrientation().subtract(orientationOffset);
	}
	
	public Vector2d getInput_Speed()
	{
		Vector2d orientation = getCenteredOrientation();
		
		if (orientation.x < -MAX_TILT_HORIZONTAL) {
			orientation.x = -MAX_SPEED_HORIZONTAL;
		}
		else if (orientation.x > MAX_TILT_HORIZONTAL) {
			orientation.x = MAX_SPEED_HORIZONTAL;
		} 
		else if(orientation.x > HORIZONTAL_TILT_LIMITER || orientation.x < -HORIZONTAL_TILT_LIMITER) {
			orientation.x *= MAX_SPEED_HORIZONTAL / (MAX_TILT_HORIZONTAL-HORIZONTAL_TILT_LIMITER);
		} 
		else {
			orientation.x = 0;
		}
		
		if (orientation.y > MAX_TILT_DOWN) {
			orientation.y = MAX_SPEED_DOWN;
		}
		else if (orientation.y > DOWN_TILT_LIMITER) {
			orientation.y *= MAX_SPEED_DOWN / (MAX_TILT_DOWN-DOWN_TILT_LIMITER);
		}
		
		
		else if (orientation.y < -MAX_TILT_UP) {
			orientation.y = -MAX_SPEED_UP;
		}
		else if (orientation.y < -UP_TILT_LIMITER) {
			orientation.y *= MAX_SPEED_UP / (MAX_TILT_UP-UP_TILT_LIMITER);
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
	
	public void addCurrency(int amount)
	{
		currency += amount;
		//Log.d("Currency changed", currency +"");
	}
	
	public void pause()
	{
		
	}
	
	public void onPaint(float deltaTime)
	{
		Graphics g = game.getGraphics();
		Vector2d pos = new Vector2d(g.getWidth()/2-hud.getWidth()/2, g.getHeight()-hud.getHeight());
		g.drawImage(hud, pos);
	}
	
	public void onUpdate(float deltaTime)
	{
		if (getInput_ShootRight()) {
			hud = hud2;
		}
		else if (getInput_ShootLeft()) {
			hud = hud1;
		}
	}
	
}
