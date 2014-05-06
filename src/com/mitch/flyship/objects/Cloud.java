package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Cloud extends GameBody {
	
	final int nClouds = 4;;
	
	int cloudType;
	Image image;
	
	public Cloud(AirshipGame game, Vector2d pos, Vector2d vel)
	{
		super(game, "Cloud", pos);
		super.velocity = vel;
		
		cloudType = (int) (Math.random() * nClouds);
		image = Assets.getImage("Clouds/cloud " + cloudType);
		super.setSize(image.getSize().scale(3.5));
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		setPos(getPos().add(velocity));
	}

	@Override
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos(), getSize());
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}

}
