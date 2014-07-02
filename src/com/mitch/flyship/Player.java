package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.util.Log;

import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Player {
	
	final double WATER_VALUE_DRAIN_TIME = 50000;
	final double WATER_VALUE = 30;
	
	final int MAX_HEALTH = 9;
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
	
	final double hudCoinSpeed = 0.001;
	
	Vector2d orientationOffset = new Vector2d(0, 3);
	AndroidGame game;
	long elapsedTime = 0;
	int currency = 0;
	boolean paused = false;
	boolean lights = false;
	boolean vibrateOnDamage = false;
	int health = MAX_HEALTH;
	double water = MAX_WATER;
	
	Image hud;
	Image hudBorder;
	Image hudCoin;
	Vector2d hudCoinStartPosRelHud = new Vector2d(5,7);
	Rect waterPositionRelHud = new Rect(191,45, 6,32);
	int waterColor = Color.rgb(67, 173, 195);
	
	List<Double> hudCoinsPercentage = new ArrayList<Double>();
	
	Vector2d hullArrowOriginRelHud = new Vector2d(102, 42);
	List<Vector2d> hullArrowOrigins = new ArrayList<Vector2d>();
	List<Image> hullArrows = new ArrayList<Image>();
	
	
	public Player(AirshipGame game)
	{
		this.game = game;
		this.hud = Assets.getImage("GUI_BASE");
		this.hudBorder = Assets.getImage("Menu/bottom border");
		this.hudCoin = Assets.getImage("hudcoin");
		
		hullArrows.add(Assets.getImage("HULL_ARROW 1"));
		hullArrows.add(Assets.getImage("HULL_ARROW 2"));
		hullArrows.add(Assets.getImage("HULL_ARROW 3"));
		hullArrows.add(Assets.getImage("HULL_ARROW 4"));
		hullArrows.add(Assets.getImage("HULL_ARROW 5"));
		
		// image | health# | flipX
		hullArrowOrigins.add(new Vector2d(6,1)); // 1 | 0 | false
		hullArrowOrigins.add(new Vector2d(6,3)); // 2 | 1 | false
		hullArrowOrigins.add(new Vector2d(5,5)); // 3 | 2 | false
		hullArrowOrigins.add(new Vector2d(3,6)); // 4 | 3 | false
		hullArrowOrigins.add(new Vector2d(1,6)); // 5 | 4 | false CENTER
		hullArrowOrigins.add(new Vector2d(0,6)); // 4 | 6 | true
		hullArrowOrigins.add(new Vector2d(0,5)); // 3 | 7 | true
		hullArrowOrigins.add(new Vector2d(0,3)); // 2 | 8 | true
		hullArrowOrigins.add(new Vector2d(0,1)); // 1 | 9 | true
		
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
		return new Rect(0,0,g.getWidth(), g.getHeight()-hud.getHeight());
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
		addWater( (int) WATER_VALUE );
	}
	
	public void addWater(int waterValue)
	{
		water += waterValue;
		water = water > MAX_WATER ? MAX_WATER : water;
	}
	
	public void addHealth(int amount)
	{
		health += amount;
	}
	
	public void applyDamage(int damage)
	{
		health -= damage;
		if (health <= 0) {
			health = 0;
			gameOver();
		}
	}
	
	public void addCurrency(int amount)
	{
		if (currency + amount < 100000) {
			currency += amount;
		} else {
			currency = 99999;
		}
		
		hudCoinsPercentage.add( 0.0 );
	}
	
	public void gameOver()
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
		Vector2d pos = new Vector2d(17, g.getHeight()-15);
		Vector2d commaSize = Assets.getImage("FONT/COIN/comma").getSize();
		Vector2d nSize = Assets.getImage("FONT/COIN/0").getSize();
		
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/10000 % 10) ), new Vector2d( nSize.x*0 + pos.x+0, pos.y));
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/1000 % 10) ), new Vector2d(  nSize.x*1 + pos.x+1, pos.y));
		
		g.drawImage(Assets.getImage("FONT/COIN/comma"), new Vector2d(nSize.x*2 + pos.x+2, nSize.y-commaSize.y+pos.y));
		
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/100 % 10) ), new Vector2d( nSize.x*2 + pos.x+5 + commaSize.x, pos.y) );
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/10 % 10) ), new Vector2d(  nSize.x*3 + pos.x+6 + commaSize.x, pos.y) );
		g.drawImage(Assets.getImage("FONT/COIN/" + (int) (currency/1 % 10) ), new Vector2d(   nSize.x*4 + pos.x+7 + commaSize.x, pos.y) );
		
	}
	
	public void drawWater()
	{
		Rect waterPosition = new Rect(waterPositionRelHud);
		double waterPercentage = water/MAX_WATER;
		
		Graphics g = game.getGraphics();
		waterPosition.height = waterPosition.height * waterPercentage;
		waterPosition.y += g.getHeight() - hud.getHeight() - hudBorder.getHeight() - waterPosition.height;
		
		g.drawRect(waterPosition, waterColor);
		
	}
	
	public void drawHudCoins()
	{
		Graphics g = game.getGraphics();
		int tubeHeight = (int) (hud.getHeight() - hudCoinStartPosRelHud.y);
		
		for (Double coinPercentage : hudCoinsPercentage) {
			Vector2d coinPos = new Vector2d( 0,g.getHeight()-hud.getHeight() ).add(hudCoinStartPosRelHud);
			
			int distancePX = (int)(tubeHeight * coinPercentage);
			g.drawImage( hudCoin, coinPos.add( new Vector2d(0, distancePX) ) );
		}
	}
	
	public void drawHullArrow()
	{
		Graphics g = game.getGraphics();
		// "Math.abs(health-9)" uses the opposite images it would normally use from health 5 to health 9
		// This gives us the arrows past the center that we need to flip.
		Image image = hullArrows.get( health > 5 ? Math.abs(health-9) : health-1 );
		Vector2d pos = new Vector2d(0,g.getHeight() - hudBorder.getHeight() - hud.getHeight() );
		pos = pos.add( hullArrowOriginRelHud.subtract(hullArrowOrigins.get(health-1)) );
		
		g.drawImage(image, pos, health > 5, false);
	}
	
	public void onPaint(float deltaTime)
	{
		Graphics g = game.getGraphics();
		
		drawWater();
		drawHudCoins();
		g.drawImage(hud, new Vector2d(0, g.getHeight()-hud.getHeight()-2));
		g.drawImage(hudBorder, new Vector2d(0, g.getHeight()-2));
		
		drawTime( (int)elapsedTime );
		drawCurrency();
		drawHullArrow();
	}
	
	public void onUpdate(float deltaTime)
	{
		elapsedTime += deltaTime;
		
		if (WATER_VALUE_DRAIN_TIME > 0) {
			water -= (WATER_VALUE / WATER_VALUE_DRAIN_TIME) * (double) deltaTime;
		}
		
		if (water <= 0) {
			water = 0;
			gameOver();
		}
		
		if (health <= 0) {
			health = 0;
			gameOver();
		}
		
		for (int i = 0; i < hudCoinsPercentage.size(); i++) {
			double percentage = hudCoinsPercentage.get(i);
			percentage += hudCoinSpeed * deltaTime;
			hudCoinsPercentage.set(i, percentage);
		}
		
		for (Double n : hudCoinsPercentage) {
			
			Log.d("HUD COIN POS", n+"");
		}
		
		if (hudCoinsPercentage.size() > 0 && hudCoinsPercentage.get(0) >= 1) {
			hudCoinsPercentage.remove(0);
		}
	}
	
	public void pause()
	{
		
	}
	
}
