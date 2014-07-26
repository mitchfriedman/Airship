package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.List;

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
	
	final Image image;
	public final int value;
	public static List<Float> spawnWeights = new ArrayList<Float>();
	
	Level level;
	
	public enum CoinType {
		GOLD,
		SILVER
	}
	
	
	public Coin(Level level, CoinType type)
	{
		super(level.getAirshipGame(), type == CoinType.GOLD ? "gold_coin" : "silver_coin");
		this.level = level;
		this.image = type == CoinType.GOLD ? Assets.getImage("gold_coin") : Assets.getImage("silver_coin");
		this.value = type == CoinType.GOLD ? 10 : 1;
		setSize(image.getSize());
		offset = image.getSize().divide(2);
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
		Vector2d goldSize = Assets.getImage("gold_coin").getSize();
		Vector2d silverSize = Assets.getImage("silver_coin").getSize();
		
		/* 0: SINGLE GOLD COIN */ 
		config = new BodyConfiguration();
		config.addConfigurationObject(goldSize.divide(2), "GOLD");
		config.setSize(goldSize);
		configurations.add(config);
		spawnWeights.add(1f);
		
		/* SINGLE SILVER COIN */
		config = new BodyConfiguration();
		config.addConfigurationObject(silverSize.divide(2), "SILVER");
		config.setSize(silverSize);
		configurations.add(config);
		spawnWeights.add(10f);
		
		/* SLIDE DOWN GOLD CENTER */
		config = new BodyConfiguration();
		config.addConfigurationObject(goldSize.scale(0).add(silverSize), "SILVER");
		config.addConfigurationObject(goldSize.scale(1).add(silverSize), "SILVER");
		config.addConfigurationObject(goldSize.scale(2).add(silverSize), "SILVER");
		config.addConfigurationObject(goldSize.scale(3).add(silverSize), "GOLD");
		config.addConfigurationObject(goldSize.scale(4).add(silverSize), "SILVER");
		config.addConfigurationObject(goldSize.scale(5).add(silverSize), "SILVER");
		config.addConfigurationObject(goldSize.scale(6).add(silverSize), "SILVER");
		config.setSize(goldSize.scale(6).add(silverSize.scale(2)));
		configurations.add(config);
		spawnWeights.add(2f);
		
		/* HORIZONTAL SILVER */
		config = new BodyConfiguration();
		config.addConfigurationObject(goldSize.scale(1).scaleX(1), "SILVER");
		config.addConfigurationObject(goldSize.scale(1).scaleX(2), "SILVER");
		config.addConfigurationObject(goldSize.scale(1).scaleX(3), "SILVER");
		config.addConfigurationObject(goldSize.scale(1).scaleX(4), "SILVER");
		config.addConfigurationObject(goldSize.scale(1).scaleX(5), "SILVER");
		config.addConfigurationObject(goldSize.scale(1).scaleX(6), "SILVER");
		config.setSize(new Vector2d(goldSize.x*6.5+silverSize.x, goldSize.y*1.5));
		configurations.add(config);
		spawnWeights.add(3f);
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
	
	public static List<GameBody> getBodiesFromConfiguration(BodyConfiguration config,
			Vector2d pos, Level level) 
	{
		List<GameBody> coins = new ArrayList<GameBody>();
		for (int i = 0; i < config.points.size(); i++) {
			CoinType coinType = config.types.get(i) == "GOLD" ? CoinType.GOLD : CoinType.SILVER;
			Coin coin = new Coin(level, coinType);
			coin.setPos(pos.add(config.points.get(i)));
			coins.add(coin);
		}
		return coins;
	}
}