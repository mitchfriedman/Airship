package com.mitch.flyship.objects;

import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Terrain extends GameBody {

	Image image;
	
	double swayLength = 30;
	double sway = 0;
	double swayDirection = 1;
	double swayTime = 1500;
	
		
	public Terrain(AndroidGame game, String name, Rect bounds) {
		super(game, name, bounds);
		this.image = Assets.getImage(name);
	}
	public Image getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = Assets.getImage(image);
	}
	@Override
	public void onUpdate(float deltaTime) {
		// TODO Auto-generated method stub
		calculateTerrainSway(deltaTime);
	}

	@Override
	public void onPaint(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
		g.drawImage(image, getBounds().getRealPosition().add(new Vector2d(sway, 0)), getBounds().getRealSize());
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
}
