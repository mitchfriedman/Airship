package com.mitch.flyship.screens;

import java.util.ArrayList;
import java.util.List;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.LevelProperties;
import com.mitch.flyship.R;
import com.mitch.flyship.objects.Cloud;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Menu extends Screen {
	
	Image platform;
	Image terrain;
	Image gear;
	
	Rect platformBounds;
	Rect terrainBounds;
	Rect gearBounds;
	
	List<Cloud> clouds;
	
	double swayLength = 30;
	double sway = 0;
	double swayDirection = 1;
	double swayTime = 1500;
	
	public Menu(AndroidGame game)
	{
		super(game);
		
		Assets.getMusic("blue").setLooping(true);
		platform = Assets.getImage("Menu/platform");
		terrain = Assets.getImage("Menu/terrain");
		gear = Assets.getImage("Menu/settingsgear");
		Graphics g = game.getGraphics();
		
		platformBounds = new Rect(0,0,0,0);
		terrainBounds = new Rect(0,0,0,0);
		gearBounds = new Rect(0,0,0,0);
		
		platformBounds.setSize(platform.getSize().scaleY(g.getWidth()));
		terrainBounds.setSize(terrain.getSize().scaleY(g.getWidth()+swayLength));
		gearBounds.setSize(gear.getSize().scaleY(40));
		
		platformBounds.setPosition(new Vector2d(0, g.getHeight()-platformBounds.height));
		gearBounds.setPosition(new Vector2d(g.getWidth()-gearBounds.width-12, 12));
		
		clouds = new ArrayList<Cloud>();
	}
	
	@Override
	public void update(float deltaTime) 
	{
		calculateTerrainSway(deltaTime);
		spawnClouds();
		
	}

	@Override
	public void paint(float deltaTime)
	{
		Graphics g = game.getGraphics();
		g.drawImage(terrain, terrainBounds.getRealPosition().add(new Vector2d(sway, 0)), terrainBounds.getRealSize());
		g.drawImage(platform, platformBounds.getRealPosition(), platformBounds.getRealSize());
		g.drawImage(gear, gearBounds.getRealPosition(), gearBounds.getRealSize());
	}
	
	void calculateTerrainSway(float deltaTime)
	{
		sway += (swayLength/swayTime)*deltaTime*swayDirection;
		sway = sway > swayLength/2 ? swayLength/2 : sway;
		sway = sway < -swayLength/2 ? -swayLength/2 : sway;
		if (sway == swayLength/2 || sway == -swayLength/2) {
			swayDirection *= -1;
		}
	}
	
	void spawnClouds()
	{
		
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

}
