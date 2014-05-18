package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class Terrain extends GameBody {

	Image image;
	
	double swayLength = 30;
	double sway = 0;
	double swayDirection = 1;
	double swayTime = 1500;
	
	List<Cloud> clouds;
	int cloudSpawnRate = 300;
	double cloudSpeed = 0.6;
	int cloudMinY;
	int cloudMaxY;
	
		
	public Terrain(AirshipGame game, String name, Rect bounds) {
		super(game, name, bounds);
		this.image = Assets.getImage(name);
		
		clouds = new ArrayList<Cloud>();
		cloudMinY = -5;
		cloudMaxY = (int) image.getHeight();
		Log.w("height:",""+image.getHeight());
	}
	public Image getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = Assets.getImage(image);
	}
	@Override
	public void onUpdate(float deltaTime) {
		spawnClouds();
		calculateTerrainSway(deltaTime);
		
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
		g.drawImage(image, getBounds().getRealPosition().add(new Vector2d(sway, 0)), getBounds().getRealSize());
		
		
		for (Cloud cloud : clouds) {
			cloud.onPaint(deltaTime);
		}
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
	public double getSwayLength() {
		return swayLength;
	}
	public void setSwayLength(double length) {
		swayLength = length;
	}
	
	private void spawnClouds()
	{
		//boolean spawnCloud = (int) (Math.random() * cloudSpawnRate) == 0;
		/*if (spawnCloud) {
			
			int cloudY = (int) (cloudMinY + (Math.random() * (cloudMaxY - cloudMinY)));
			Vector2d pos = new Vector2d(0, cloudY);
			Vector2d vel = new Vector2d(cloudSpeed, 0);
			//Cloud cloud = new Cloud(game, vel);
			cloud.setPos(pos);
			cloud.setPos(cloud.getPos().subtract(cloud.getSize()));
			clouds.add(cloud);
		}*/
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}
}
