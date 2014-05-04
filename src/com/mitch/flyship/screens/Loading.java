package com.mitch.flyship.screens;

import com.mitch.flyship.Assets;
import com.mitch.flyship.LevelProperties;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;

public class Loading extends Screen {
	
	double splashDisplayTime = 200.000;
	double elapsedTime = 0;
	double opacity = 0;
	Game game;
	Image splash;
	
	public Loading(Game game) {
		super(game);
		this.game = game;
		
		Graphics g = game.getGraphics();
		Assets.loadImage("WIG_Splash", "WIG/WIG_splash.png", null, g);
		splash = Assets.getImage("WIG_Splash");
	}

	@Override
	public void update(float deltaTime) 
	{
		elapsedTime += deltaTime;
		double percentage = elapsedTime / splashDisplayTime;
		percentage = percentage > 1 ? 1 : percentage;
		opacity = 255 * percentage;
		
		if (Assets.isLoaded() && elapsedTime > splashDisplayTime) {
			game.setScreen(new Level(game, new LevelProperties()));
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB( 255, 0, 0, 0);
		g.drawImage(splash, 100, 250);
		g.drawARGB( (int) opacity, 0, 0, 0);
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
