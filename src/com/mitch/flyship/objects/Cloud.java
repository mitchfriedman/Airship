package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Image;

public class Cloud extends GameBody {
	
	final int nClouds = 4;;
	
	int cloudType;
	Image image;
	
	public Cloud(AirshipGame game)
	{
		super(game, "Cloud");
		cloudType = (int) (Math.random() * nClouds);
		image = Assets.getImage("Clouds/cloud " + cloudType);
	}
	
	@Override
	public void onUpdate(float deltaTime) {

	}

	@Override
	public void onPaint(float deltaTime) {

	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}

}
