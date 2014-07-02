package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Water extends GameBody {
	
	Level level;
	final Image image;
	
	
	
	public Water(Level level)
	{
		super(level.getAirshipGame(), "WATER");
		
		this.level = level;
		this.image = Assets.getImage("water");
		super.offset = image.getSize().divide(2);
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		
		if (getPos().y > game.getGraphics().getHeight()) {
			level.getBodyManager().removeBody(this);
		}
	}

	@Override
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos().subtract(offset));
	}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}
	
	public static List<GameBody> spawnObjects(Level level, String type)
	{
		Graphics g = level.getAirshipGame().getGraphics();
		double xPos = Math.random()*( g.getWidth() - Assets.getImage("water").getWidth() );
		double yPos = -Assets.getImage("water").getHeight();
		
		Water water = new Water(level);
		water.setPos(new Vector2d(xPos, yPos));
		
		List<GameBody> bodies = new ArrayList<GameBody>();
		bodies.add(water);
		return bodies;
	}
}