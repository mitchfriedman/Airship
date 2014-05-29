package com.mitch.flyship.objects;


import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Cloud extends GameBody {
	
	static final int N_CLOUDS = 9;
	
	Level level;
	Image image;
	int cloudType;
	
	public Cloud(Level level, Vector2d vel)
	{
		super(level.getAirshipGame(), "Cloud");
		super.velocity = vel;
		this.level = level;
		
		cloudType = (int) (Math.random() * N_CLOUDS);
		image = Assets.getImage("Clouds/cloud " + cloudType);
		setSize(image.getSize());
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		setPos(getPos().add(velocity));
		
		Graphics g = game.getGraphics();
		if (getPos().x > g.getWidth() || getPos().y > g.getHeight()) {
			level.getBodyManager().removeBody(this);
		}
	}

	@Override
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos());
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}
	
	public static List<GameBody> spawnObjects(Level level, String type)
	{
		Log.d("SPAWNING CLOUD", "k?");
		Cloud cloud = new Cloud(level, new Vector2d(0,1));
		Vector2d cloudSize = cloud.getSize();
		Graphics g = level.getAirshipGame().getGraphics();
		cloud.setPos(new Vector2d(Math.random() * (g.getWidth()+cloudSize.x*2) - cloudSize.x, -cloudSize.y));
		
		List<GameBody> clouds = new ArrayList<GameBody>();
		clouds.add(cloud);
		return clouds;
	}

}
