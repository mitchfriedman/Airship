package com.mitch.flyship.screens;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.LevelProperties;
import com.mitch.flyship.ObjectSpawner;
import com.mitch.flyship.Player;
import com.mitch.flyship.Popup;
import com.mitch.flyship.Preferences;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.SliderMoveListener;
import com.mitch.flyship.levelmanagers.LevelBodyManager;
import com.mitch.flyship.levelmanagers.LevelSpawnerManager;
import com.mitch.flyship.objects.Button;
import com.mitch.flyship.objects.Cloud;
import com.mitch.flyship.objects.Coin;
import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.objects.Ship;
import com.mitch.flyship.objects.Water;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidFastRenderView;


public class Level extends Screen {

	enum GameState {
		OVER,
		PAUSED,
		RUNNING
	}

	static final float BUTTON_ANIMATION_TIME_IN_MS = 350;
	int backgroundHeight = 0;
	Image backgroundImage;
	double backgroundPos = 0;
	final LevelBodyManager bm;
	List<Button> buttons = new ArrayList<Button>();
	float buttonXOffset = 0;
	float elapsedTime = 0;
	float elapsedTimeAfterPauseInMS = 0;
	ButtonClickListener gearListener, hangarListener, settingsListener, resumeListener, calibrateListener;
	boolean isSpawning = true;
	LevelProperties lvlInfo;
	Popup options;
	List<Button> pauseButtons = new ArrayList<Button>();
	SliderMoveListener sensitivityListener;
	//final LevelEventManager em;
	final LevelSpawnerManager sm;
	double speed;	
	GameState state = GameState.RUNNING;


	// Creates endless level
	public Level(AirshipGame game)
	{
		super(game);
		lvlInfo = new LevelProperties();
		generateListeners();

		bm = new LevelBodyManager();
		sm = new LevelSpawnerManager(this, true);
		//em = new LevelEventManager(this);
		//em.loadEvents();

		setBackgroundImage("Background/riverterrain");
		setSpeed(1.5);

		Enemy.generateDictionary(this);
		generateShip();
		generateSpawners();
		generatePauseState();

	}

	@Override
	public void backButton() 
	{
		if (state == GameState.RUNNING) {
			game.setScreen(new Menu(game));
		} else if (state == GameState.PAUSED && options.isEnabled()) {
			options.setEnabled(false);
		} else if (state == GameState.PAUSED) {
			state = GameState.RUNNING;
		}
	}

	@Override
	public void dispose() {}

	public double distanceToTime(double distance)
	{
		return distance / (getSpeed() * AndroidFastRenderView.UPS);
	}

	private void generateListeners()
	{
		calibrateListener = new ButtonClickListener() {
			@Override
			public void onCancel() { }
			@Override
			public void onDown() { }
			@Override
			public void onUp() 
			{
				bm.getShip().getPlayer().centerOrientation();
			}
		};
		resumeListener = new ButtonClickListener() {
			@Override
			public void onCancel() { }
			@Override
			public void onDown() { }
			@Override
			public void onUp() { 
				state = GameState.RUNNING;
			}
		};

		sensitivityListener = new SliderMoveListener() {
			@Override
			public void onDown() {}
			@Override
			public void onPositionChanged(float position) {
				Preferences.putSensitivityInPercent(position);
			}
			@Override
			public void onUp() {}
		};

		settingsListener = new ButtonClickListener() {
			@Override
			public void onCancel() { }
			@Override
			public void onDown() { }
			@Override
			public void onUp() {
				options.setEnabled(true);
			}
		};
		gearListener = new ButtonClickListener() {
			@Override
			public void onCancel() { }
			@Override
			public void onDown() { }
			@Override
			public void onUp() { 
				toggleLevelPauseState();
			}
		};
		hangarListener = new ButtonClickListener() {
			@Override
			public void onCancel() { }
			@Override
			public void onDown() { }
			@Override
			public void onUp() { backButton(); }
		};
	}

	private void generatePauseState()
	{
		Graphics g = game.getGraphics();
		Align alignment;
		Vector2d position;


		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
		position = new Vector2d(g.getWidth()-8, 8);
		buttons.add(new Button(game, "GUI/Gear", alignment, position, gearListener));

		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
		position = new Vector2d(0,0);
		Button settingsButton = new Button(game, "GUI/Settings Button", alignment, position, settingsListener);
		settingsButton.setPos(new Vector2d(g.getWidth(), settingsButton.getImage().getHeight()));
		pauseButtons.add(settingsButton);

		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
		position = new Vector2d(g.getWidth(), settingsButton.getImage().getHeight()*2 + 15);
		pauseButtons.add(new Button(game, "GUI/Hangar Button", alignment, position, hangarListener));

		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
		position = new Vector2d(g.getWidth(), settingsButton.getImage().getHeight()*3 + 30);
		pauseButtons.add(new Button(game, "GUI/Resume Button", alignment, position, resumeListener));

		options = new Popup(game);

		options.addImage("GUI/tilt sensitivity", Align.Horizontal.CENTER);
		options.addSlider(Preferences.retrieveSensitivityInPercent(), sensitivityListener);

		options.setMargin(20);
		options.addImage("GUI/tilt calibration", Align.Horizontal.CENTER);
		options.setMargin(20);
		options.addButton(calibrateListener, "GUI/Calibrate");
	}

	private void generateShip()
	{
		ShipParams params = game.loadMerchantShipParams();
		Player player = new Player(game);
		Vector2d centerScreen = game.getGraphics().getSize().scale(0.5);
		Ship ship = new Ship(this, player, params, centerScreen);
		bm.setShip(ship);
	}

	private void generateSpawners()
	{
		sm.addSpawner( new ObjectSpawner("COIN", Coin.class, timeToDistance(275), timeToDistance(1250)) );
		sm.addSpawner( new ObjectSpawner("WATER", Water.class, timeToDistance(15000), timeToDistance(20000)) );
		sm.addSpawner( new ObjectSpawner("CLOUD", Cloud.class, timeToDistance(250), timeToDistance(2500)) );

		// Spawns enemies
		sm.addSpawner(new ObjectSpawner("BIRD",    Enemy.class, timeToDistance(2000), timeToDistance(10000)));
		sm.addSpawner(new ObjectSpawner("FIGHTER", Enemy.class, timeToDistance(2000), timeToDistance(10000)));
		sm.addSpawner(new ObjectSpawner("GUNSHIP", Enemy.class, timeToDistance(2000), timeToDistance(10000)));
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

	public double getSpeed() 
	{
		return speed;
	}

	public void onLevelPause()
	{
		bm.getShip().getPlayer().onLevelPause();
	}

	public void onLevelResume()
	{
		bm.getShip().getPlayer().onLevelResume();
	}

	@Override
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

		for(Button button: buttons) {
			button.onPaint(deltaTime);
		}

		switch(state) {
		case RUNNING:
			paintRunning(deltaTime);
			break;
		case PAUSED:
			paintPaused(deltaTime);
			break;
		case OVER:
			paintOver(deltaTime);
			break;
		}

	}

	private void paintOver(float deltaTime) {

	}

	private void paintPaused(float deltaTime) 
	{
		for(Button button: pauseButtons) {
			button.onPaint(deltaTime);
		}
		options.paint(deltaTime);
	}

	private void paintRunning(float deltaTime) {

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

	public void setBackgroundImage(String image) 
	{
		backgroundImage = Assets.getImage(image);
		backgroundHeight = backgroundImage.getHeight();
	}

	public void setSpeed(double speed) 
	{
		this.speed = speed;
	}

	public double timeToDistance(float time)
	{
		return time/1000 * (AndroidFastRenderView.UPS * getSpeed());
	}

	public void toggleLevelPauseState()
	{
		if (state == GameState.PAUSED) {
			state = GameState.RUNNING;
			onLevelResume();
		}
		else {
			state = GameState.PAUSED;
			onLevelPause();
		}
	}

	@Override
	public void update(float deltaTime) 
	{

		switch(state) {
		case RUNNING:
			updateRunning(deltaTime);
			break;
		case PAUSED:
			updatePaused(deltaTime);
			break;
		case OVER:
			updateOver(deltaTime);
			break;
		}
	}

	public void updateOver(float deltaTime)
	{

	}

	private void updatePaused(float deltaTime)
	{

		options.update(deltaTime);

		if (!options.isEnabled()) {
			for (Button button : pauseButtons) {
				button.onUpdate(deltaTime);
			}

			for (Button button : buttons) {
				button.onUpdate(deltaTime);
			}
		}


	}

	private void updateRunning(float deltaTime) 
	{
		for (Button button : buttons) {
			button.onUpdate(deltaTime);
		}

		// Once you've touch somebody's heart, you'll be as cool as 
		// the rest of the cold hearted murderers out there
		elapsedTime += deltaTime;

		//updates all bodies in body manager (ship, enemies, items)
		bm.onUpdate(deltaTime);
		bm.offsetBodies(new Vector2d(0, speed));

		// Sets off events
		//em.update();

		// Updates background position
		backgroundPos += getSpeed();
		backgroundPos = backgroundPos > backgroundHeight ? 0 : backgroundPos;

		// Spawns objects from LevelSpawningManager. Iterates through all spawners, 
		// checks if spawner can spawn, Instantiates however that object spawns and
		// adds them to LevelBodyManager.
		for (ObjectSpawner spawner : sm.getSpawners()) {
			spawner.updateDistance(getSpeed());
			if (spawner.canSpawn() && isSpawning) {
				List<GameBody> bodyList = spawner.trySpawnObjects(this);
				for (GameBody body : bodyList) {
					getBodyManager().addBody(body);
				}
				spawner.resetDistanceInfo();
			}
		}
	}



}
