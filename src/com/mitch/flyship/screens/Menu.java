package com.mitch.flyship.screens;

import com.mitch.flyship.Assets;
import com.mitch.flyship.objects.Button;
import com.mitch.framework.Game;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Vector2d;

public class Menu extends Screen {
	
	Button button;
	
	public Menu(Game game)
	{
		super(game);
		//Graphics g = game.getGraphics();
		button = new Button(game, new Vector2d(200, 200), "LEVEL 1");
	}
	
	@Override
	public void update(float deltaTime) 
	{
		button.onUpdate(deltaTime);
	}

	@Override
	public void paint(float deltaTime) 
	{
		button.onPaint(deltaTime);
	}

	@Override
	public void pause() {
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
