package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Terrain extends GameBody {

	Image image;
	
	double swayLength = 0; // This is set in the constructor
	double sway = 0;
	double elapsedTime = 0;
	double swayTime = 50000;
	
	List<Cloud> clouds;
	int cloudSpawnRate = 300;
	double cloudSpeed = 0.6;
	int cloudMinY;
	int cloudMaxY;
	
		
	public Terrain(AirshipGame game, String name) {
		super(game, name);
		this.image = Assets.getImage(name);
		
		swayLength = image.getWidth() - 201;
		
		clouds = new ArrayList<Cloud>();
		cloudMinY = -5;
		cloudMaxY = game.getGraphics().getHeight() - Assets.getImage("Menu/platform").getHeight();
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = Assets.getImage(image);
	}
	
	@Override
	public void onUpdate(double deltaTime) {
		
		elapsedTime += deltaTime;
		calculateTerrainSway(deltaTime);

		spawnClouds();
		
		
		// We add clouds to a remove list because we can't delete the clouds from the list
		// while we're iterating through it.
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
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		
		
		g.drawImage(image, getPos().x-sway, getPos().y);
		
		for (Cloud cloud : clouds) {
			cloud.onPaint(deltaTime);
		}
	}
	
	void calculateTerrainSway(double deltaTime)
	{
		double amountOfSways = elapsedTime / swayTime;
		double swayPercentage = amountOfSways - Math.floor(amountOfSways);
		sway = swayLength*2 * Math.abs(0.5-swayPercentage);
	}
	
	public double getSwayLength() {
		return swayLength;
	}
	
	public void setSwayLength(double length) {
		swayLength = length;
	}
	
	private void spawnClouds()
	{
		boolean spawnCloud = (int) (Math.random() * cloudSpawnRate) == 0;
		if (spawnCloud) {
			
			int cloudY = (int) (cloudMinY + (Math.random() * (cloudMaxY - cloudMinY)));
			Vector2d pos = new Vector2d(0, cloudY);
			Vector2d vel = new Vector2d(cloudSpeed, 0);
			Cloud cloud = new Cloud(game, vel);
			cloud.setPos(pos);
			cloud.setPos(cloud.getPos().subtract(cloud.getSize()));
			clouds.add(cloud);
		}
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}
}
