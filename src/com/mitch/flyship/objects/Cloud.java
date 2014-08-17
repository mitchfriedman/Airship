package com.mitch.flyship.objects;


import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidFastRenderView;

public class Cloud extends GameBody {
	
	static final int N_CLOUDS = 9;
	
	Level level;
	Image image;
	int cloudType;
	
	public Cloud(AirshipGame game, Vector2d vel)
	{
		super(game, "Cloud");
		
		cloudType = (int) (Math.random() * N_CLOUDS);
		image = Assets.getImage("Clouds/cloud " + cloudType);
		setVelocity(vel);
		setSize(image.getSize());
	}
	
	public Cloud(Level level, Vector2d vel)
	{
		this(level.getAirshipGame(), vel);
		this.level = level;
	}
	
	@Override
	public void onUpdate(double deltaTime) {
        Vector2d velocity = this.velocity.scale( 1000 / (AndroidFastRenderView.UPS * deltaTime) );
		setPos(getPos().add(velocity));
		
		Graphics g = game.getGraphics();
		if (level != null && getPos().x > g.getWidth() || getPos().y > g.getHeight()) {
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
		Cloud cloud = new Cloud(level, new Vector2d(0,1));
		cloud.setDepth( 149 + (int) (Math.random()*2) );
		
		Graphics g = level.getAirshipGame().getGraphics();
		Vector2d cloudSize = cloud.getSize();
		cloud.setPos(new Vector2d(Math.random() * (g.getWidth()+cloudSize.x*2) - cloudSize.x, -cloudSize.y));
		
		List<GameBody> clouds = new ArrayList<GameBody>();
		clouds.add(cloud);
		return clouds;
	}

}
