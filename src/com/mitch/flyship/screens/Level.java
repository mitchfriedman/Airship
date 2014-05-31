package com.mitch.flyship.screens;

import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.ObjectSpawner;
import com.mitch.flyship.Player;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.levelmanagers.LevelBodyManager;
import com.mitch.flyship.levelmanagers.LevelEventManager;
import com.mitch.flyship.levelmanagers.LevelSpawnerManager;
import com.mitch.flyship.objects.Cloud;
import com.mitch.flyship.objects.Coin;
import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Vector2d;


public class Level extends Screen {
	
	double speed;
	Image backgroundImage;
	double backgroundPos = 0;
	float elapsedTime = 0;
	int backgroundHeight = 0;
	final LevelBodyManager bm;
	final LevelEventManager em;
	final LevelSpawnerManager sm;
	
	
	// Creates endless level
	public Level(AirshipGame game)
	{
		super(game);
		
		bm = new LevelBodyManager();
		em = new LevelEventManager(this);
		sm = new LevelSpawnerManager(this, true);
		//lem.loadEvents();
		
		Enemy.generateDictionary(this);
		
		ShipParams params = game.loadMerchantShipParams();
		Player player = new Player(game);
		Vector2d centerScreen = game.getGraphics().getSize().scale(0.5);
		Ship ship = new Ship(this, player, params, centerScreen);
		bm.setShip(ship);
		
		setBackgroundImage("Background/riverterrain");
		setSpeed(1);
		
		
		sm.addSpawner(new ObjectSpawner("COIN", Coin.class, 275f, 1250f));
		sm.addSpawner(new ObjectSpawner("CLOUD", Cloud.class, 250f, 2500f));
		
		// Spawns enemies
		sm.addSpawner(new ObjectSpawner("BIRD", Enemy.class, 2000f, 10000f));
		sm.addSpawner(new ObjectSpawner("FIGHTER", Enemy.class, 2000f, 10000f));
		sm.addSpawner(new ObjectSpawner("GUNSHIP", Enemy.class, 2000f, 10000f));
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
	boolean isSpawning = true;
	public void update(float deltaTime) 
	{
		// Once you've touch somebody's heart, you'll be as cool as 
		// the rest of the cold hearted murderers out there
		elapsedTime += deltaTime;
		
		//updates all bodies in body manager (ship, enemies, items)
		bm.onUpdate(deltaTime);
		bm.offsetBodies(new Vector2d(0, speed));
		
		// Sets off events
		em.update();
		
		// Updates background position
		backgroundPos += getSpeed();
		backgroundPos = backgroundPos > backgroundHeight ? 0 : backgroundPos;
		
		// Spawns objects from LevelSpawningManager. Iterates through all spawners, 
		// checks if spawner can spawn, Instantiates however that object spawns and
		// adds them to LevelBodyManager.
		for (ObjectSpawner spawner : sm.getSpawners()) {
			spawner.updateTime(deltaTime);
			if (spawner.canSpawn() && isSpawning) {
				List<GameBody> bodyList = spawner.trySpawnObjects(this);
				for (GameBody body : bodyList) {
					getBodyManager().addBody(body);
				}
				spawner.resetTimeInfo();
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
	public void pause() 
	{
		Assets.getMusic("blue").pause();
	}
	
	@Override
	public void resume() 
	{
		Assets.getMusic("blue").play();
	}
	
	@Override
	public void dispose() {}
	
	@Override
	public void backButton() 
	{
		game.setScreen(new Menu(game));
	}
	
	

}
