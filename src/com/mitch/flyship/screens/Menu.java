package com.mitch.flyship.screens;

import android.content.res.XmlResourceParser;
import android.graphics.Paint.Align;

import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.LevelProperties;
import com.mitch.flyship.R;
import com.mitch.flyship.objects.Button;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Menu extends Screen {
	
	Button button;
	Image splashLogo;
	
	ButtonClickListener levelOneClick = new ButtonClickListener() {
		
		@Override
		public void onUp() {
			XmlResourceParser xrp = game.getResources().getXml(R.xml.level1);
			LevelProperties properties = new LevelProperties(xrp);
			game.setScreen(new Level(game, properties));
		}
		
		@Override
		public void onDown() {}
	};
	
	public Menu(AndroidGame game)
	{
		super(game);
		Graphics g = game.getGraphics();
		button = new Button(game, new Vector2d(g.getWidth()/2, 375), "Menu/Button/lvl1", 50, Align.CENTER, levelOneClick);
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
