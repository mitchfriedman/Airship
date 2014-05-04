package com.mitch.flyship.screens;

import android.util.Log;

import com.mitch.flyship.Assets;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;

public class Level extends Screen {
	
	Game game;
	public int background1Y, background2Y;
	private int backgroundSpeed;
	public Image backgroundImage;
	
	final int IMAGE_HEIGHT = 2336;
	
	public Level(Game game) {
		super(game);
		this.game = game;
		background1Y = 0;
		background2Y = -IMAGE_HEIGHT;
		
		setBackgroundImage();
	}
	
	public void loadXML(String file) {
		
	}
	public void setBackgroundImage() {
		/* parse XML and load background image */
		Graphics g = game.getGraphics();
		Assets.loadImage("background", "Backgrounds/sky.png", null, g);
		backgroundImage = Assets.getImage("background");
		setSpeed(1);
	}
	
	public void setSpeed(int speed) {
		backgroundSpeed = speed;
	}
	public int getSpeed() {
		return backgroundSpeed;
	}
	
	public void update(float deltaTime) {
		background1Y += backgroundSpeed;
		background2Y += backgroundSpeed;
		
		if(background1Y >= IMAGE_HEIGHT) {
			background1Y -= 2 * IMAGE_HEIGHT;
		}
		if(background2Y >= IMAGE_HEIGHT) {
			background2Y -= 2 * IMAGE_HEIGHT;
		}
		
		
	}
	
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB( 255, 0, 0, 0);
		g.drawImage(backgroundImage, 0, background1Y);
		g.drawImage(backgroundImage, 0, background2Y);
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void backButton() {
		// TODO Auto-generated method stub
		
	}

}
