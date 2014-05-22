package com.mitch.flyship.screens;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;

public class Loading extends Screen {
	
	double splashDisplayTime = 1800; //180 works best
	double elapsedTime = 0;
	double opacity = 0;
	Image splash;
	
	public Loading(AirshipGame game) 
	{
		super(game);
		
		Graphics g = game.getGraphics();
		Assets.loadImage("WIG_Splash", "WIG_splash1.png", null, g);
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
			game.setScreen(new Menu(game));
			//game.setScreen(new Level(game));
		}
		
	}

	@Override
	public void paint(float deltaTime) 
	{
		Graphics g = game.getGraphics();
		// draws image at center.
		g.drawARGB( 255, 0, 0, 0);
		g.drawImage(splash, g.getWidth()/2-splash.getWidth()/2, g.getHeight()/2-splash.getHeight()/2);
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
