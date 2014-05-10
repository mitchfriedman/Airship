package com.mitch.flyship.screens;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.LevelProperties;
import com.mitch.flyship.Ship;
import com.mitch.framework.LevelBodyManager;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;


public class Level extends Screen {
	
	public LevelBodyManager bm;
	
	Game game;
	double speed;
	Image backgroundImage;
	int backgroundPos = 0;
	int backgroundHeight = 0;
	private Ship ship;
	
	public Level(AirshipGame game, LevelProperties properties) 
	{
		super(game);
		this.game = game;
		
		bm = new LevelBodyManager(ship);
		loadFromProperties(properties);
	}
	
	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public void loadFromProperties(LevelProperties properties) 
	{
		setBackgroundImage(properties.background);
		setSpeed(properties.speed);
	}
	
	public void setBackgroundImage(String image) 
	{
		backgroundImage = Assets.getImage(image);
		double width  = backgroundImage.getWidth();
		double height = backgroundImage.getHeight();
		backgroundHeight = (int) (height * (game.getGraphics().getWidth() / width));
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
		backgroundPos += getSpeed();
		backgroundPos = backgroundPos > backgroundHeight ? 0 : backgroundPos;
	}
	
	public void paint(float deltaTime) 
	{
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
