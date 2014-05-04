package com.mitch.flyship.screens;

import com.mitch.flyship.Assets;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;


public class Level extends Screen {
	static final int BACKGROUND_IMAGE_HEIGHT = 2336;
	
	Game game;
	public int backgroundPos = 0;
	public Image backgroundImage;
	private double speed;
	
	public Level(Game game) {
		super(game);
		this.game = game;
		backgroundPos = 0;
		
		setBackgroundImage("background");
		setSpeed(1.00);
	}
	
	public void loadXML(String file) {
		
	}
	
	public void setBackgroundImage(String image) {
		/* parse XML and load background image */
		Graphics g = game.getGraphics();
		backgroundImage = Assets.getImage("background");
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getSpeed() {
		return speed;
	}
	
	public void update(float deltaTime) {
		
		backgroundPos += getSpeed();
		backgroundPos = backgroundPos > BACKGROUND_IMAGE_HEIGHT ? 0 : backgroundPos;
	}
	
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB( 255, 0, 0, 0);
		g.drawImage(backgroundImage, 0, backgroundPos);
		g.drawImage(backgroundImage, 0, backgroundPos-BACKGROUND_IMAGE_HEIGHT);
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
