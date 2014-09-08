package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Matrix;

import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Input;
import com.mitch.framework.containers.MathHelper;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class Player {
	
	static final double DEFAULT_PITCH_OFFSET = 25;
	static final boolean USE_MATRIX = true;
	
	final double WATER_VALUE_DRAIN_TIME = 40;
	final double WATER_VALUE = 30;
	
	final int MAX_HEALTH = 9; //9
	final int MAX_WATER = 90;
	
	final double MAX_TILT_UP = 35;
	final double MAX_TILT_DOWN = 35;
	final double MAX_TILT_HORIZONTAL = 40;
	
	private final double MAX_SPEED_UP = 80;
	private final double MAX_SPEED_DOWN = 80;
	private final double MAX_SPEED_HORIZONTAL = 70;
	
	final double UP_TILT_LIMITER = 0.2;
	final double DOWN_TILT_LIMITER = 0.2;
	final double HORIZONTAL_TILT_LIMITER = 0.4;
	
	final double HULL_FLASH_INTERVAL = 0.5;
	final int HULL_FLASH_START_HEALTH = 4;
	
	final double hudCoinSpeed = 2;
	
	boolean invulnerable = false;
	double tiltSensitivity = 1;
	
	Vector2d orientationOffset = new Vector2d(0,0);
    Matrix xMatrix; // Two options for input: simple math with degrees
    Matrix yMatrix; // or convert degrees into vectors and transform them with matrices
    
    
	AirshipGame game;
    Level level;
	float elapsedTime = 0;
	int currency = 0;
	boolean vibrateOnDamage = false;
	int health = MAX_HEALTH;
	double water = MAX_WATER;
	
	Image hud;
	Image hudBorder;
	Image hudCoin;
    double hudStart;
    
    double lastHullFlash = 0;
    boolean hullFlashDrawn = false;
    Image hullFlashImage;
    Vector2d hullFlashLocationRelHud = new Vector2d(97,33);
    
	Rect waterPositionRelHud = new Rect(191,45, 6,32);
    Rect waterPosition = new Rect(0,0,0,0);
	int waterColor = Color.rgb(67, 173, 195);

    Vector2d hudCoinWorldPos;
    int coinTubeHeight;
    Vector2d hudCoinStartPosRelHud = new Vector2d(5,7);
	List<Double> hudCoinsPercentage = new ArrayList<Double>();
    List<Vector2d> hudCoinsPos = new ArrayList<Vector2d>();

	Vector2d hullArrowOriginRelHud = new Vector2d(102, 42);
	List<Vector2d> hullArrowOrigins = new ArrayList<Vector2d>();
	List<Image> hullArrows = new ArrayList<Image>();
    Image hullArrowImage;
    Vector2d hullArrowPosition = new Vector2d(0,0);
	
	
	public Player(Level level)
	{
        this.level = level;
		this.game = level.getAirshipGame();
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
		
		hullArrowImage = hullArrows.get(0);
		hullFlashImage = Assets.getImage("GUI/hull flash");
		
        Graphics g = game.getGraphics();
        hudStart = g.getHeight() - hud.getHeight() - hudBorder.getHeight();

        this.hudCoinWorldPos = new Vector2d( 0,hudStart ).add(hudCoinStartPosRelHud);
        this.coinTubeHeight = (int) (hud.getHeight() - hudCoinStartPosRelHud.y);
        
        if (game.getCenterOrientation() != null) {
        	setOrientationOffset(game.getCenterOrientation());
        } else {
            setOrientationOffset(new Vector2d(180, DEFAULT_PITCH_OFFSET+90));
        }
        
        resetSensitivity();
        
	}
	
	double convertPercentToSensitivity(float percent)
	{
		double max = AirshipGame.MAX_SENSITIVITY;
		double min = AirshipGame.MIN_SENSITIVITY;
		return max - (max - min) * percent;
	}
	
	public void setOrientationOffset(Vector2d offset)
	{
		if (USE_MATRIX) {
			xMatrix = new Matrix();
			xMatrix.setRotate((float) offset.x);
			
			yMatrix = new Matrix();
			yMatrix.setRotate((float) (360-offset.y));
		} else {
			// simple degrees
		}
		
	}
	
	public double getWaterPercent()
	{
		return water/MAX_WATER;
	}
	
	public void centerOrientation()
	{
		setOrientationOffset(getOrientation());
	}
	
	public Vector2d getOrientation()
	{
		Input input = game.getInput();
		return new Vector2d(input.GetTiltX(), input.GetTiltY());
	}
	
	public Rect getShipBounds()
	{
		Graphics g = game.getGraphics();
		return new Rect(0,0,g.getWidth(), g.getHeight()-hud.getHeight());
	}
	
	public Vector2d getCenteredOrientation()
	{
		Vector2d orientation = getOrientation();
		double degreeX, degreeY;
		
		if (USE_MATRIX) {
			float[] xVector = new float[] {
				(float) Math.cos(Math.toRadians(orientation.x)),
				(float) Math.sin(Math.toRadians(orientation.x)),
			};
			
			float[] yVector = new float[] {
				(float) Math.cos(Math.toRadians(orientation.y)),
				(float) Math.sin(Math.toRadians(orientation.y)),
			};
			
			xMatrix.mapPoints(xVector);
			yMatrix.mapPoints(yVector);
			
			degreeX = MathHelper.convertToAngle(xVector[0], xVector[1]);
			degreeY = MathHelper.convertToAngle(yVector[0], yVector[1]);
		} else {
			degreeX = 0;
			degreeY = 0;
		}
		
		Vector2d centeredOrientation = new Vector2d(degreeX, degreeY);
		//Log.d("Centered Orientation", centeredOrientation.x + ", " + centeredOrientation.y);
		return centeredOrientation;
		
	}

    public double getMaxSpeedHorizontal(double deltaTime)
    {
        return MAX_SPEED_HORIZONTAL * deltaTime;
    }

    public double getMaxSpeedDown(double deltaTime)
    {
        return MAX_SPEED_DOWN * deltaTime;
    }

    public double getMaxSpeedUp(double deltaTime)
    {
        return MAX_SPEED_UP * deltaTime;
    }

    public Vector2d getInput_Speed(double deltaTime)
    {
        return getInput_Speed(deltaTime, false, false);
    }
    
	public Vector2d getInput_Speed(double deltaTime, boolean invertVertical, boolean invertHorizontal)
	{
		Vector2d orientation = getCenteredOrientation();
		final double ts = tiltSensitivity;
		
        orientation.y *= invertVertical ? -1 : 1;
        orientation.x *= invertHorizontal ? -1 : 1;

		if (orientation.x < -MAX_TILT_HORIZONTAL*ts) {
			orientation.x = -MAX_SPEED_HORIZONTAL*deltaTime;
		}
		else if (orientation.x > MAX_TILT_HORIZONTAL*ts) {
			orientation.x = MAX_SPEED_HORIZONTAL*deltaTime;
		} 
		else if(orientation.x > HORIZONTAL_TILT_LIMITER*ts || orientation.x < -HORIZONTAL_TILT_LIMITER*ts) {
			orientation.x *= (MAX_SPEED_HORIZONTAL*deltaTime) / (MAX_TILT_HORIZONTAL*ts-HORIZONTAL_TILT_LIMITER*ts);
		} 
		else {
			orientation.x = 0;
		}

		
		if (orientation.y > MAX_TILT_DOWN*ts) {
			orientation.y = MAX_SPEED_DOWN*deltaTime;
		}
		else if (orientation.y > DOWN_TILT_LIMITER*ts) {
			orientation.y *= (MAX_SPEED_DOWN*deltaTime) / (MAX_TILT_DOWN*ts-DOWN_TILT_LIMITER*ts);
		}
		
		else if (orientation.y < -MAX_TILT_UP*ts) {
			orientation.y = -MAX_SPEED_UP*deltaTime;
		}
		else if (orientation.y < -UP_TILT_LIMITER*ts) {
			orientation.y *= (MAX_SPEED_UP*deltaTime) / (MAX_TILT_UP*ts-UP_TILT_LIMITER*ts);
		}
		
		/*if (orientation.y > -UP_TILT_LIMITER*ts && orientation.y < DOWN_TILT_LIMITER*ts) {
			orientation.y = 0;
		}*/
		
		return orientation;
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
		if(health + amount <= MAX_HEALTH) {
			health += amount;
		} else {
			health = MAX_HEALTH;
		}	
	}
	
	public void setInvulnerable(boolean invulnerable)
	{
		this.invulnerable = invulnerable;
	}
	
	public void applyDamage(int damage)
	{

		if (!invulnerable) {
			health -= damage;
	        game.Vibrate(damage*150);
		}
		
		if (health <= 1) {
			health = 1;
			gameOver(Level.DeathReason.CRASH);
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

    public int getCurrency()
    {
        return currency;
    }
    
    public double getElapsedTime_s()
    {
    	return elapsedTime;
    }
	
	public void gameOver(Level.DeathReason deathReason)
	{
        game.getGoogleAPIManager().connect();
        game.pushScore(level.getLeaderboardID(), (long) getCurrency());
        level.end(deathReason);
	}
	
	public void drawTime(int time)
	{
		int elapsedSeconds = time;
		int minutes = (int) (elapsedSeconds / 60);
		int seconds = (int) (elapsedSeconds % 60);

		int width = 8;
		Graphics g = game.getGraphics();
		g.drawImage(Assets.getImage("FONT/TIMER/" + (int) (minutes / 10) ), new Vector2d( width*0 + 5, 5));
		g.drawImage(Assets.getImage("FONT/TIMER/" + (int) (minutes % 10) ), new Vector2d( width*1 + 5, 5));
		
		g.drawImage(Assets.getImage("FONT/TIMER/:"), new Vector2d(width*2+6,8));
		
		g.drawImage(Assets.getImage("FONT/TIMER/" + (int) (seconds / 10) ), new Vector2d( width*3 + 5, 5));
		g.drawImage(Assets.getImage("FONT/TIMER/" + (int) (seconds % 10) ), new Vector2d( width*4 + 5, 5));
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
		Graphics g = game.getGraphics();
		g.drawRect(waterPosition, waterColor);
	}
	
	public void drawHudCoins()
	{
		Graphics g = game.getGraphics();
		for (Vector2d hudCoinPos : hudCoinsPos) {
			g.drawImage( hudCoin, hudCoinPos);
		}
	}
	
	public void drawHullArrow()
	{
		Graphics g = game.getGraphics();
		
		g.drawImage(hullArrowImage, hullArrowPosition, health > 5, false);
	}
	
	public void onPaint(float deltaTime)
	{
		Graphics g = game.getGraphics();
		
		drawWater();
		drawHudCoins();
		g.drawImage(hud, new Vector2d(0, g.getHeight()-hud.getHeight()-2));
		g.drawImage(hudBorder, new Vector2d(0, g.getHeight()-2));
		
		//drawTime( (int)elapsedTime );
		drawCurrency();
		if (hullFlashDrawn) {
			g.drawImage(hullFlashImage, new Vector2d(0, g.getHeight()-hud.getHeight()-2).add(hullFlashLocationRelHud));
		}
		
		drawHullArrow();
	}
	
	public void onUpdate(double deltaTime)
	{
		elapsedTime += deltaTime;
		lastHullFlash += deltaTime;
		
		// Water consumption increases with level speed
		if (WATER_VALUE_DRAIN_TIME != 0) {
			double waterValueDrainDistance = Level.calculateDistanceTravelled(WATER_VALUE_DRAIN_TIME, level.getLevelSpeed());
			water -= (WATER_VALUE / waterValueDrainDistance) * Level.timeToDistance(deltaTime, level.getLevelSpeed());
		}
		
		if (water <= 0) {
			water = 0;
			gameOver(Level.DeathReason.LACK_OF_WATER);
		}
		
		if (health <= 0) {
			health = 0;
			gameOver(Level.DeathReason.CRASH);
		}
		
		if (health <= HULL_FLASH_START_HEALTH && lastHullFlash > HULL_FLASH_INTERVAL) {
			hullFlashDrawn = !hullFlashDrawn;
			lastHullFlash = 0;
		} else if (health > HULL_FLASH_START_HEALTH) {
			hullFlashDrawn = false;
		}
		
        waterPosition = new Rect(waterPositionRelHud);
        double waterPercentage = water/MAX_WATER;

        Graphics g = game.getGraphics();
        waterPosition.height = waterPosition.height * waterPercentage;
        waterPosition.y += g.getHeight() - hud.getHeight() - hudBorder.getHeight() - waterPosition.height;

        hudCoinsPos.clear();
		for (int i = 0; i < hudCoinsPercentage.size(); i++) {
			double percentage = hudCoinsPercentage.get(i);
			percentage += hudCoinSpeed * deltaTime;
			hudCoinsPercentage.set(i, percentage);

            int distancePX = (int)(coinTubeHeight * hudCoinsPercentage.get(i));
            hudCoinsPos.add(hudCoinWorldPos.add( new Vector2d(0, distancePX) ));
		}
		
		if (hudCoinsPercentage.size() > 0 && hudCoinsPercentage.get(0) >= 1) {
			hudCoinsPercentage.remove(0);
		}

        hullArrowImage = hullArrows.get( health > 5 ? Math.abs(health-9) : health-1 );
        Vector2d hullArrowOrigin = hullArrowOrigins.get(health-1);
        hullArrowPosition =
                new Vector2d(0, hudStart)       // Start of hud
                .add(hullArrowOriginRelHud)     // Center of hull arrow relative hud
                .subtract(hullArrowOrigin);     // Center of arrow image relative top left
	}
	
	public void resetSensitivity()
	{
		tiltSensitivity = convertPercentToSensitivity((float) game.getSensitivity());
	}
	public void pause()
	{
		
	}
	
}
