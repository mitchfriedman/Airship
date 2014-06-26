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
	
	Vector2d orientationOffset = new Vector2d(0, 3);
	AndroidGame game;
	long elapsedTime = 0;
	int currency = 0;
	boolean paused = false;
	boolean lights = false;
	int health = MAX_HEALTH;
	int water = MAX_WATER;
	
	Image hud;
	
	public Player(AirshipGame game)
	{
		this.game = game;
		this.hud = Assets.getImage("GUI_BASE");
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
		return new Rect(0,0,g.getWidth(), g.getHeight());
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
		if (currency + amount < 100000) {
			currency += amount;
		} else {
			currency = 99999;
		}
		
	}
	
	public void pause()
	{
		
	}
	
	public void drawTime(int msTime)
	{
		int elapsedSeconds = msTime/1000;
		int minutes = (int) (elapsedSeconds / 60);
		int seconds = (int) (elapsedSeconds % 60);

		int width = 8;
		Graphics g = game.getGraphics();
		g.drawImage(Assets.getImage("FONT/TIMER/" + (int) (minutes / 10) ), new Vector2d( width*0 + 4, 5));
		g.drawImage(Assets.getImage("FONT/TIMER/" + (int) (minutes % 10) ), new Vector2d( width*1 + 4, 5));
		
		g.drawImage(Assets.getImage("FONT/TIMER/colon"), new Vector2d(width*2+6,8));
		
		g.drawImage(Assets.getImage("FONT/TIMER/" + (int) (seconds / 10) ), new Vector2d( width*3 + 4, 5));
		g.drawImage(Assets.getImage("FONT/TIMER/" + (int) (seconds % 10) ), new Vector2d( width*4 + 4, 5));
	}
	
	public void drawCurrency()
	{
		Graphics g = game.getGraphics();
		Vector2d pos = new Vector2d(17, g.getHeight()-13);
		Vector2d commaSize = Assets.getImage("FONT/COIN/comma").getSize();
		Vector2d nSize = Assets.getImage("FONT/COIN/0").getSize();
		
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/10000 % 10) ), new Vector2d( nSize.x*0 + pos.x+0, pos.y));
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/1000 % 10) ), new Vector2d(  nSize.x*1 + pos.x+1, pos.y));
		
		g.drawImage(Assets.getImage("FONT/COIN/comma"), new Vector2d(nSize.x*2 + pos.x+2, nSize.y-commaSize.y+pos.y));
		
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/100 % 10) ), new Vector2d( nSize.x*2 + pos.x+5 + commaSize.x, pos.y) );
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/10 % 10) ), new Vector2d(  nSize.x*3 + pos.x+6 + commaSize.x, pos.y) );
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/1 % 10) ), new Vector2d(   nSize.x*4 + pos.x+7 + commaSize.x, pos.y) );
		
	}
	
	public void onPaint(float deltaTime)
	{
		Graphics g = game.getGraphics();
		g.drawImage(hud, new Vector2d(0, g.getHeight()-hud.getHeight()));
		
		drawTime( (int)elapsedTime );
		drawCurrency();
	}
	
	public void onUpdate(float deltaTime)
	{
		elapsedTime += deltaTime;
	}
	
}
