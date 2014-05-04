package com.mitch.flyship.screens;

import com.mitch.flyship.Assets;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Vector2d;

public class Loading extends Screen {
	
	double splashDisplayTime = 200.000;
	double elapsedTime = 0;
	double opacity = 0;
	Game game;
	Image splash;
	Vector2d size;
	
	public Loading(Game game) {
		super(game);
		this.game = game;
		
		Graphics g = game.getGraphics();
		Assets.loadImage("WIG_Splash", "WIG/WIG_splash.png", null, g);
		splash = Assets.getImage("WIG_Splash");
		
		size = splash.getSize();
		size = size.scaleY(350);
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
		}
		
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawARGB( 255, 0, 0, 0);
		g.drawImage(splash, g.getWidth()/2-size.x/2, 250, size.x, size.y);
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
