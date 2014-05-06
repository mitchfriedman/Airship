package com.mitch.flyship.screens;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.objects.Cloud;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class Menu extends Screen {
	
	Image platform;
	Image terrain;
	Image gear;
	
	Rect platformBounds;
	Rect terrainBounds;
	Rect gearBounds;
	
	List<Cloud> clouds;
	int cloudSpawnRate = 300;
	double cloudSpeed = 0.6;
	int cloudMinY;
	int cloudMaxY;
	
	
	double swayLength = 30;
	double sway = 0;
	double swayDirection = 1;
	double swayTime = 1500;
	
	public Menu(AirshipGame game)
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
		cloudMinY = -5;
		cloudMaxY = platformBounds.getIntPosition().y;
	}
	
	@Override
	public void update(float deltaTime) 
	{
		calculateTerrainSway(deltaTime);
		spawnClouds();
		
		List<Cloud> removeClouds = new ArrayList<Cloud>();
		for (Cloud cloud : clouds) {
			if (cloud.getPos().x > game.getGraphics().getWidth()) {
				removeClouds.add(cloud);
			}
		}
		
		for (Cloud cloud : removeClouds) {
			clouds.remove(cloud);
		}
		
		for (Cloud cloud : clouds) {
			cloud.onUpdate(deltaTime);
		}
	}

	@Override
	public void paint(float deltaTime)
	{
		Graphics g = game.getGraphics();
		g.drawImage(terrain, terrainBounds.getRealPosition().add(new Vector2d(sway, 0)), terrainBounds.getRealSize());
		
		for (Cloud cloud : clouds) {
			cloud.onPaint(deltaTime);
		}
		
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
		boolean spawnCloud = (int) (Math.random() * cloudSpawnRate) == 0;
		if (spawnCloud) {
			
			int cloudY = (int) (cloudMinY + (Math.random() * (cloudMaxY - cloudMinY)));
			Vector2d pos = new Vector2d(0, cloudY);
			Vector2d vel = new Vector2d(cloudSpeed, 0);
			Cloud cloud = new Cloud(game, pos, vel);
			cloud.setPos(cloud.getPos().subtract(cloud.getSize()));
			clouds.add(cloud);
		}
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
