package com.mitch.flyship.screens;

import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.BodyConfiguration;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.Player;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.objects.Cloud;
import com.mitch.flyship.objects.Coin;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.LevelBodyManager;
import com.mitch.framework.LevelEventManager;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Vector2d;


public class Level extends Screen {
	
	double speed;
	Image backgroundImage;
	double backgroundPos = 0;
	float elapsedTime = 0;
	int backgroundHeight = 0;
	final LevelBodyManager bm;
	final LevelEventManager lem;
	
	// Creates endless level
	public Level(AirshipGame game)
	{
		super(game);
		
		bm = new LevelBodyManager();
		lem = new LevelEventManager(this);
		lem.loadEvents();
		
		ShipParams params = game.loadMerchantShipParams();
		Player player = new Player(game);
		Vector2d centerScreen = game.getGraphics().getSize().scale(0.5);
		Ship ship = new Ship(this, player, params, centerScreen);
		bm.setShip(ship);
		
		setBackgroundImage("Background/riverterrain");
		setSpeed(1);
	}
	
	/*public Level(AirshipGame game, LevelProperties properties) 
	{
		super(game);
		
		bm = new LevelBodyManager();
		loadFromProperties(properties);
	}
	
	public void loadFromProperties(LevelProperties properties) 
	{
		setBackgroundImage(properties.background);
		setSpeed(properties.speed);
		
		Player player = new Player(game);
		ShipParams params = new ShipParams("dirigible");
		Vector2d shipPos = new Vector2d(game.getGraphics().getWidth(), game.getGraphics().getHeight());
		Ship ship = new Ship(this, player, params, shipPos);
		
		bm.setShip(ship);
	}*/
	
	public void setBackgroundImage(String image) 
	{
		backgroundImage = Assets.getImage(image);
		backgroundHeight = backgroundImage.getHeight();
	}
	
	public void setSpeed(double speed) 
	{
		this.speed = speed;
	}
	
	public double getSpeed() 
	{
		return speed;
	}
	
	public LevelBodyManager getBodyManager()
	{
		return bm;
	}
	
	public float getElapsedTime_ms()
	{
		return elapsedTime;
	}
	
	public float getElapsedTime_s()
	{
		return elapsedTime/1000;
	}
	
	public void update(float deltaTime) 
	{
		elapsedTime += deltaTime;
		lem.update();
		
		//updates all bodies in body manager (ship, enemies, items)
		bm.onUpdate(deltaTime);
		bm.offsetItems(new Vector2d(0, speed));
		
		backgroundPos += getSpeed();
		backgroundPos = backgroundPos > backgroundHeight ? 0 : backgroundPos;
		
		Graphics g = game.getGraphics();
		
		if ((int)(Math.random() * 100) == 0) {
			Cloud cloud = new Cloud(this, new Vector2d(0,1));
			Vector2d cloudSize = cloud.getSize();
			cloud.setPos(new Vector2d(Math.random() * (g.getWidth()-cloudSize.x), -cloudSize.y));
			bm.addBodyToItems(cloud);
		}
		
		// Randomly spawns coins (temporary)
		if ((int)(Math.random() * 100) == 0) {
			int coinConfigID = (int) (Math.random() * Coin.getBodyConfigurationCount());
			BodyConfiguration coinConfig = Coin.getBodyConfiguration(coinConfigID);
			
			Vector2d coinConfigSize = coinConfig.getConfigurationSize();
			Vector2d coinConfigPos = new Vector2d(Math.random()*(g.getWidth()-coinConfigSize.x), -coinConfigSize.y);
			List<GameBody> coins = Coin.getBodiesFromConfiguration(coinConfig, coinConfigPos, this);
			for (GameBody coin : coins) {
				bm.addBodyToItems(coin);
			}
		}
	}
	
	public void paint(float deltaTime) 
	{
		Graphics g = game.getGraphics();
		g.drawARGB( 255, 0, 0, 0);
		if (backgroundPos < g.getHeight()) {
			g.drawImage(backgroundImage, 0, backgroundPos);
		}
		g.drawImage(backgroundImage, 0, backgroundPos-backgroundHeight);
		
		//paints all bodies in body manager (ship, enemies, items)
		bm.onPaint(deltaTime);
	}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	@Override
	public void dispose() {}
	
	@Override
	public void backButton() {}
	
	

}
