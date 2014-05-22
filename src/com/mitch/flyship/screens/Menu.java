package com.mitch.flyship.screens;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.objects.Button;
import com.mitch.flyship.objects.Platform;
import com.mitch.flyship.objects.Terrain;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class Menu extends Screen {
	
	Image buttonImage;
	
	Button endlessMode;
	Button missionsMode;
	Button shopMode;
	ButtonClickListener endlessListener;
	ButtonClickListener missionsListener;
	ButtonClickListener shopListener;
	
	Platform platform;
	Terrain terrain;
	
	Button gear;
	ButtonClickListener gearListener;
	

	public Menu(AirshipGame game)
	{
		super(game);
		
		Assets.getMusic("blue").setLooping(true);
		platform = new Platform(game, "Menu/platform");
		terrain = new Terrain(game, "Menu/terrain");
		
		gearListener = new ButtonClickListener() {
			@Override
			public void onUp() { }
			@Override
			public void onDown() { }
			@Override
			public void onDepressed() { }
			@Override
			public void onCancel() { }
		};		
		endlessListener = new ButtonClickListener() {
			@Override
			public void onUp() { }
			@Override
			public void onDown() { }
			@Override
			public void onDepressed() { }
			@Override
			public void onCancel() { }
		};
		missionsListener = new ButtonClickListener () {
			@Override
			public void onUp() { }
			@Override
			public void onDown() { }
			@Override
			public void onDepressed() { }
			@Override
			public void onCancel() { }
		};
		shopListener = new ButtonClickListener() {
			@Override
			public void onUp() { }
			@Override
			public void onDown() { }
			@Override
			public void onDepressed() { }
			@Override
			public void onCancel() { }
		};
		Graphics g = game.getGraphics();
		gear = new Button(game, "GUI/Gear",  new Align(Align.Vertical.BOTTOM, Align.Horizontal.CENTER), gearListener);
		gear.setPos(new Vector2d(g.getWidth()-gear.getImage().getWidth(), 0));
		
		endlessMode = new Button(game, "Menu/Buttons/Endless", new Align(Align.Vertical.TOP, Align.Horizontal.LEFT), endlessListener);
		endlessMode.setPos(new Vector2d(g.getWidth()/2-endlessMode.getImage().getWidth()/2, g.getHeight()-endlessMode.getImage().getHeight()));
		
		missionsMode = new Button(game, "Menu/Buttons/Missions", new Align(Align.Vertical.TOP, Align.Horizontal.LEFT), missionsListener);
		missionsMode.setPos(new Vector2d(0, g.getHeight()-missionsMode.getImage().getHeight()));
		
		shopMode = new Button(game, "Menu/Buttons/Shop", new Align(Align.Vertical.TOP, Align.Horizontal.LEFT), shopListener);
		shopMode.setPos(new Vector2d(g.getWidth()-shopMode.getImage().getWidth(), g.getHeight()-shopMode.getImage().getHeight()));
		
	}
	
	@Override
	public void update(float deltaTime) 
	{	
		terrain.onUpdate(deltaTime);	
		endlessMode.onUpdate(deltaTime);
		missionsMode.onUpdate(deltaTime);
		shopMode.onUpdate(deltaTime);
		gear.onUpdate(deltaTime);
	}

	@Override
	public void paint(float deltaTime)
	{	
		terrain.onPaint(deltaTime);
		platform.onPaint(deltaTime);		
		endlessMode.onPaint(deltaTime);
		missionsMode.onPaint(deltaTime);
		shopMode.onPaint(deltaTime);
		gear.onPaint(deltaTime);
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
