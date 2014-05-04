package com.mitch.flyship.screens;

import android.graphics.Paint.Align;

import com.mitch.flyship.Assets;
import com.mitch.flyship.objects.Button;
import com.mitch.framework.Game;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Vector2d;

public class Menu extends Screen {
	
	Button button;
	Image splashLogo;
	
	
	public Menu(Game game)
	{
		super(game);
		Graphics g = game.getGraphics();
		button = new Button(game, new Vector2d(g.getWidth()/2, 375), "Menu/Button/lvl1", 50, Align.CENTER);
		Assets.getMusic("blue").setLooping(true);
		splashLogo = Assets.getImage("Menu/splash_logo");
	}
	
	@Override
	public void update(float deltaTime) 
	{
		button.onUpdate(deltaTime);
	}

	@Override
	public void paint(float deltaTime)
	{
		Graphics g = game.getGraphics();
		button.onPaint(deltaTime);
		Vector2d size = splashLogo.getSize();
		size = size.scaleY(400);
		g.drawImage(splashLogo, g.getWidth()/2-size.x/2, 65, size.x, size.y);
	}

	@Override
	public void pause() 
	{
		Assets.getMusic("blue").pause();
	}

	@Override
	public void resume() {
		Assets.getMusic("blue").play();
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void backButton() {
		
	}

}
