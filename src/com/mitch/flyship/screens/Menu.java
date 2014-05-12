package com.mitch.flyship.screens;

import android.graphics.Paint.Align;
import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.objects.Button;
import com.mitch.flyship.objects.Platform;
import com.mitch.flyship.objects.Terrain;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
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
		platform = new Platform(game, "Menu/platform", new Rect(0,0,0,0));//Assets.getImage("Menu/platform");
		terrain = new Terrain(game, "Menu/terrain", new Rect(0,0,0,0));
		
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
		gear = new Button(game, "settings", new Vector2d(680,20), "Menu/Gear Button", 40, Align.RIGHT, gearListener);

		endlessMode = new Button(game, "endless", new Vector2d(350,660), "Menu/Buttons/Endless Button", 100, Align.CENTER, endlessListener);
		missionsMode = new Button(game, "missions", new Vector2d(200,780), "Menu/Buttons/Missions Button", 100, Align.RIGHT, missionsListener);
		shopMode = new Button(game, "shop", new Vector2d(520,780), "Menu/Buttons/Shop Button", 100, Align.RIGHT, shopListener);
		
		Graphics g = game.getGraphics();
		platform.setSize(platform.getImage().getSize().scaleY(g.getWidth()));
		terrain.setSize(terrain.getImage().getSize().scaleY(g.getWidth()+terrain.getSwayLength()));

		platform.setPos(new Vector2d(0, g.getHeight()-platform.getBounds().height));

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
