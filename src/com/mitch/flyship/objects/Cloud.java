package com.mitch.flyship.objects;


import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.MathHelper;
import com.mitch.framework.containers.Vector2d;

public class Cloud extends GameBody {
	
	static final int N_CLOUDS = 8;
	static final Float[] cloudWeights = new Float[] {
		4f, //0
		4f, //1
		6f, //2
		0.75f, //3
		3f, //4
		6f, //5
		6f, //6
		4f  //7
	};
	
	
	Level level;
	Image image;
	int cloudType;
	
	public Cloud(AirshipGame game, Vector2d vel, int cloudType)
	{
		super(game, "CLOUD");
		this.cloudType = cloudType;
		image = Assets.getImage("Clouds/cloud " + cloudType);
		setVelocity(vel);
		setSize(image.getSize());
	}
	
	public Cloud(AirshipGame game, Vector2d vel)
	{
		this(game, vel, (int) (Math.random() * N_CLOUDS));
	}
	
	public Cloud(Level level, Vector2d vel)
	{
		this(level.getAirshipGame(), vel);
		this.level = level;
	}
	
	public Cloud(Level level, Vector2d vel, int cloudType)
	{
		this(level.getAirshipGame(), vel, cloudType);
		this.level = level;
	}
	
	@Override
	public void onUpdate(double deltaSeconds) {;
		setPos(getPos().add(velocity.scale(deltaSeconds)));
		
		Graphics g = game.getGraphics();
		if ( (level != null) && (getPos().x > g.getWidth() || getPos().y > g.getHeight()) ) {
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
		List<Float> weights = new ArrayList<Float>(N_CLOUDS);
		for (int i = 0; i < cloudWeights.length; i++) {
			weights.add(cloudWeights[0]);
		}
		
		int cloudType = MathHelper.generateRandomValueFromWeights(weights);
		Cloud cloud = new Cloud(level, new Vector2d(0,8), cloudType);
		cloud.setDepth( 149 + (int) (Math.random()*2) );
		
		Graphics g = level.getAirshipGame().getGraphics();
		Vector2d cloudSize = cloud.getSize();
		cloud.setPos(new Vector2d(Math.random() * (g.getWidth()+cloudSize.x*2) - cloudSize.x, -cloudSize.y));
		
		List<GameBody> clouds = new ArrayList<GameBody>();
		clouds.add(cloud);
		return clouds;
	}

}
