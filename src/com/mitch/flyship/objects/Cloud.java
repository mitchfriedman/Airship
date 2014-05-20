package com.mitch.flyship.objects;


import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Cloud extends GameBody {
	
	static final int N_CLOUDS = 8;
	
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

}
