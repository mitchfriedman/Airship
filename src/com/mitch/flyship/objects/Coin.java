package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.mitch.flyship.Assets;
import com.mitch.flyship.BodyConfiguration;
import com.mitch.flyship.BodySpawner;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Coin extends GameBody {
	static List<BodyConfiguration> configurations;
	
	private final static String ONE = "ONE";
	private final static String FIVE = "FIVE";
	private final static String TEN = "TEN";
	
	final Image image;
	public final int value;
	public static List<Float> spawnWeights = new ArrayList<Float>();
	
	Level level;
	
	public enum CoinType {
		ONE,
		FIVE,
		TEN
	}
	
	
	public Coin(Level level, CoinType type)
	{
		super(level.getAirshipGame(), "COIN-" + type.toString());
		this.level = level;
		
		switch (type) {
		default:
		case ONE:
			this.image = Assets.getImage("coin-1");
			this.value = 1;
			break;
		case FIVE:
			this.image = Assets.getImage("coin-5");
			this.value = 5;
			break;
		case TEN:
			this.image = Assets.getImage("coin-10");
			this.value = 10;
			break;
		}
		
		setSize(this.image.getSize());
		offset = this.image.getSize().divide(2);
	}
	
	@Override
	public void onUpdate(double deltaTime) {
		
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
	
	public boolean hasCoinInRange(double distance)
	{
		List<Coin> coins = level.getBodyManager().getBodiesByClass(Coin.class);
		for (Coin coin : coins) {
			if (coin != this && coin.getPos().getDistance(getPos()) <= distance) {
				return true;
			}
		}
		return false;
	}
	
	public static int getBodyConfigurationCount()
	{
		if (configurations == null) {
			generateBodyConfigurations();
		}
		
		return configurations.size();
	}
	
	static void generateBodyConfigurations()
	{
		configurations = new ArrayList<BodyConfiguration>();
		spawnWeights = new ArrayList<Float>();
		
		BodyConfiguration config;
		Vector2d size1 = Assets.getImage("coin-1").getSize();
		Vector2d size5 = Assets.getImage("coin-5").getSize();
		Vector2d size10 = Assets.getImage("coin-10").getSize();
		
		// SINGLE TEN COIN  
		config = new BodyConfiguration();
		config.addConfigurationObject(size10.divide(2), TEN);
		config.setSize(size10);
		configurations.add(config);
		spawnWeights.add(85f);
		
		// SINGLE FIVE COIN
		config = new BodyConfiguration();
		config.addConfigurationObject(size1.divide(2), FIVE);
		config.setSize(size5);
		configurations.add(config);
		spawnWeights.add(93f);
		
		// SINGLE ONE COIN
		config = new BodyConfiguration();
		config.addConfigurationObject(size1.divide(2), ONE);
		config.setSize(size1);
		configurations.add(config);
		spawnWeights.add(115f);
		
		// SLIDE DOWN ONES WITH FIVE CENTER
		config = new BodyConfiguration();
		config.addConfigurationObject(size10.scale(0).add(size1), ONE);
		config.addConfigurationObject(size10.scale(1).add(size1), ONE);
		config.addConfigurationObject(size10.scale(2).add(size1), ONE);
		config.addConfigurationObject(size10.scale(3).add(size1), FIVE);
		config.addConfigurationObject(size10.scale(4).add(size1), ONE);
		config.addConfigurationObject(size10.scale(5).add(size1), ONE);
		config.addConfigurationObject(size10.scale(6).add(size1), ONE);
		config.setSize(size10.scale(6).add(size1.scale(2)));
		configurations.add(config);
		spawnWeights.add(90f);
		
		// SLIDE DOWN ONES WITH TEN CENTER
		config = new BodyConfiguration();
		config.addConfigurationObject(size10.scale(0).add(size1), ONE);
		config.addConfigurationObject(size10.scale(1).add(size1), ONE);
		config.addConfigurationObject(size10.scale(2).add(size1), ONE);
		config.addConfigurationObject(size10.scale(3).add(size1), TEN);
		config.addConfigurationObject(size10.scale(4).add(size1), ONE);
		config.addConfigurationObject(size10.scale(5).add(size1), ONE);
		config.addConfigurationObject(size10.scale(6).add(size1), ONE);
		config.setSize(size10.scale(6).add(size1.scale(2)));
		configurations.add(config);
		spawnWeights.add(80f);
		
		// SLIDE DOWN FIVES WITH TEN CENTER
		config = new BodyConfiguration();
		config.addConfigurationObject(size10.scaleY(6).scaleX(0), FIVE);
		config.addConfigurationObject(size10.scaleY(5).scaleX(1), FIVE);
		config.addConfigurationObject(size10.scaleY(4).scaleX(2), FIVE);
		config.addConfigurationObject(size10.scaleY(3).scaleX(3), TEN);
		config.addConfigurationObject(size10.scaleY(2).scaleX(4), FIVE);
		config.addConfigurationObject(size10.scaleY(1).scaleX(5), FIVE);
		config.addConfigurationObject(size10.scaleY(0).scaleX(6), FIVE);
		config.setSize(size10.scale(7));
		configurations.add(config);
		spawnWeights.add(45f);
		
		// HORIZONTAL ONES
		config = new BodyConfiguration();
		config.addConfigurationObject(size10.scale(1).scaleX(1), ONE);
		config.addConfigurationObject(size10.scale(1).scaleX(2), ONE);
		config.addConfigurationObject(size10.scale(1).scaleX(3), ONE);
		config.addConfigurationObject(size10.scale(1).scaleX(4), ONE);
		config.addConfigurationObject(size10.scale(1).scaleX(5), ONE);
		config.addConfigurationObject(size10.scale(1).scaleX(6), ONE);
		config.setSize(new Vector2d(size10.x*6.5+size1.x, size10.y*1.5));
		configurations.add(config);
		spawnWeights.add(94f);
		
		// STAR!!!
		config = new BodyConfiguration();
		Bitmap image = Assets.getImage("star_coin_map").getBitmap();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (image.getPixel(x, y) == Color.BLACK) {
					config.addConfigurationObject(new Vector2d(x,y).scale(size5.x), ONE);
				}
			}
		}
		config.setSize(new Vector2d(image.getWidth(), image.getHeight()).scale(size5.x));
		configurations.add(config);
		spawnWeights.add(1f);
		
		
	}
	
	public static List<GameBody> spawnObjects(Level level, String type)
	{
		int configID = BodySpawner.generateRandomValueFromWeights(spawnWeights);
		BodyConfiguration config = getBodyConfiguration(configID);
		
		Graphics g = level.getAirshipGame().getGraphics();
		double xPos = Math.random()*(g.getWidth()-config.getConfigurationSize().x);
		double yPos = -config.getConfigurationSize().y;
		Vector2d pos = new Vector2d(xPos, yPos);
		return getBodiesFromConfiguration(config, pos, level);
	}
	
	public static BodyConfiguration getBodyConfiguration(int configID)
	{
		/* CREATES CONFIGURATIONS IF THEY DON'T ALREADY EXIST. */
		if (configurations == null) {
			generateBodyConfigurations();
		}
		
		return configurations.get(configID);
	}
	
	public Image getImage() {
		return image;
	}
	
	public static List<GameBody> getBodiesFromConfiguration(BodyConfiguration config,
			Vector2d pos, Level level) 
	{
		List<GameBody> coins = new ArrayList<GameBody>();
		for (int i = 0; i < config.points.size(); i++) {
			Coin coin = new Coin(level, CoinType.valueOf(config.types.get(i)));
			coin.setPos(pos.add(config.points.get(i)));
			coins.add(coin);
		}
		return coins;
	}
}