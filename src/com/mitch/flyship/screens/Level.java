package com.mitch.flyship.screens;

import com.mitch.flyship.Assets;
import com.mitch.flyship.LevelProperties;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.implementation.AndroidGame;


public class Level extends Screen {
	
	Game game;
	int backgroundHeight = 0;
	int backgroundPos = 0;
	double speed;
	Image backgroundImage;
	
	public Level(AndroidGame game, LevelProperties properties) 
	{
		super(game);
		this.game = game;
		
		//setBackgroundImage("Background/lvl1");
		//setSpeed(20);
		loadFromProperties(properties);
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
