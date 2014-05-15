package com.mitch.flyship.screens;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.LevelProperties;
import com.mitch.flyship.Player;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.LevelBodyManager;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Vector2d;


public class Level extends Screen {
	
	double speed;
	Image backgroundImage;
	int backgroundPos = 0;
	int backgroundHeight = 0;
	LevelBodyManager bm;
	
	// Creates endless level
	public Level(AirshipGame game)
	{
		super(game);
		
		bm = new LevelBodyManager();
		Ship ship = game.loadMerchantShip();
		bm.setShip(ship);
		setBackgroundImage("Background/ocean");
		setSpeed(1);
	}
	
	public Level(AirshipGame game, LevelProperties properties) 
	{
		super(game);
		
		bm = new LevelBodyManager();
		loadFromProperties(properties);
	}
	
	public void loadFromProperties(LevelProperties properties) 
	{
		setBackgroundImage(properties.background);
		setSpeed(properties.speed);
		
		
		Player player = new Player(game);
		ShipParams params = new ShipParams("dirigible");
		Vector2d shipPos = new Vector2d(game.getGraphics().getWidth(), game.getGraphics().getHeight());
		Ship ship = new Ship(this, player, params, shipPos);
		bm.setShip(ship);
	}
	
	public void setBackgroundImage(String image) 
	{
		backgroundImage = Assets.getImage(image);
		backgroundHeight = backgroundImage.getHeight();
	}
	
	public void setSpeed(double speed) 
	{
		this.speed = speed;
	}
	
	public double getSpeed() 
	{
		return speed;
	}
	
	public void update(float deltaTime) 
	{
		//updates all bodies in body manager
		bm.onUpdate(deltaTime);
		
		backgroundPos += getSpeed();
		backgroundPos = backgroundPos > backgroundHeight ? 0 : backgroundPos;
	}
	
	public void paint(float deltaTime) 
	{
		//paints all bodies in body manager (ship, enemies, items)
		bm.onPaint(deltaTime);
		
		Graphics g = game.getGraphics();
		g.drawARGB( 255, 0, 0, 0);
		g.drawImage(backgroundImage, 0, backgroundPos, g.getWidth(), backgroundHeight);
		g.drawImage(backgroundImage, 0, backgroundPos-backgroundHeight, g.getWidth(), backgroundHeight);
	}
	
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	@Override
	public void dispose() {}
	
	@Override
	public void backButton() {}

}
