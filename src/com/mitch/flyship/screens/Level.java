package com.mitch.flyship.screens;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.BodySpawner;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.LevelProperties;
import com.mitch.flyship.Player;
import com.mitch.flyship.Popup;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.Enemy.EnemyProperties;
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
import com.mitch.framework.Music;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Align.Horizontal;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidFastRenderView;


public class Level extends Screen {

	enum GameState {
		OVER,
		PAUSED,
		RUNNING
	}
	
	public enum DeathReason {
		CRASH,
		LACK_OF_WATER
	}

    public List<EnemyProperties> getEnemyTypes()
    {
        return enemyTypes;
    }

    public LevelBodyManager getBodyManager()
    {
        return bm;
    }

    private LevelProperties properties;

    private Music music;
    private Image backgroundImage;
    private int backgroundHeight = 0;
    private double backgroundPos = 0;
    private double speed = 0;

    private List<Button> buttons = new ArrayList<Button>();
    private List<Button> pauseButtons = new ArrayList<Button>();
    private ButtonClickListener pauseListener, hangarListener, 
    							settingsListener, resumeListener, muteListener, 
    							leaderboardsListener, restartListener;

    private LevelBodyManager bm;
    private LevelSpawnerManager sm;

    private GameState state = GameState.RUNNING;
    private double lastUpdate = 0;
    private double elapsedTime = 0;
	private boolean isSpawning = true;

    private int maxLevel;
    private double timeToMaxLevel;
    private double startSpeed;
    private double endSpeed;
    private int currentLevel = 0;
    private boolean maxLevelCap = true;
    private boolean includeCloudAtEnd = true;

	private Popup options;
    private Popup endPopup;

    List<EnemyProperties> enemyTypes;

	public Level(AirshipGame game)
	{
		this(game, new LevelProperties());
	}

    public Level(AirshipGame game, LevelProperties properties)
    {
        super(game);

        this.properties = properties;

        generateListeners();
        generateButtons();
        generate(properties);
    }

    private void generate(LevelProperties properties)
    {
        if (AirshipGame.DEBUG) {
            Log.d("Level", "Generating level " + properties.getName());
        }

        bm = new LevelBodyManager();
        sm = new LevelSpawnerManager(this, true);

        this.currentLevel = 1;
        this.startSpeed = properties.getStartSpeed();
        this.endSpeed = properties.getEndSpeed();
        this.maxLevel = properties.getMaxLevel();
        this.timeToMaxLevel = properties.getTimeToMaxLevel();
        calculateLevelSpeed();

        this.enemyTypes = properties.getEnemyTemplates();
        setBackgroundImage(properties.getBackground());
        setMusic(properties.getMusic());

        //Enemy.generateDictionary(this);
        generateShip(properties.getShip());
        generateSpawners(properties);
        generatePauseState();

        lastUpdate = System.nanoTime();
    }

    private void generateButtons()
    {
        Graphics g = game.getGraphics();
        Align alignment;
        Vector2d position;

        alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
        position = new Vector2d(g.getWidth()-8, 8);
        buttons.add(new Button(game, "GUI/Gear", alignment, position, pauseListener));

        alignment = new Align(Align.Vertical.TOP, Align.Horizontal.LEFT);
        position = new Vector2d(g.getWidth()-50, 8);
        Button button = new Button(game, "GUI/mute", alignment, position, muteListener);
        //button.setToggled(AirshipGame.muted);
        pauseButtons.add(button);

    }

    private void generatePauseState()
    {
        // This whole thing could be done with an XML file but fuck you.
        Graphics g = game.getGraphics();
        Align alignment;
        Vector2d position;

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

        options = Popup.buildSettingsPopup(game);
    }

    private void generateShip(String shipID)
    {
        ShipParams params = game.loadMerchantShipParams();
        Player player = new Player(this);
        Vector2d centerScreen = game.getGraphics().getSize().scale(0.5);
        Ship ship = new Ship(this, player, params, centerScreen);
        bm.setShip(ship);
    }

    private void generateSpawners(LevelProperties props)
    {

        sm.addSpawner( new BodySpawner(Coin.class, "COIN",
                timeToDistance(275),
                timeToDistance(1250)) );
        sm.addSpawner( new BodySpawner(Water.class, "WATER",
                timeToDistance(props.getWaterSpawnTimeStart()*1000),
                timeToDistance(props.getWaterSpawnTimeEnd()*1000)) );
        sm.addSpawner( new BodySpawner(Cloud.class, "CLOUD",
                timeToDistance(250),
                timeToDistance(2500)) );

        for (EnemyProperties property : enemyTypes) {

            List<Integer> spawnRange = new ArrayList<Integer>();
            spawnRange.addAll(property.getSpawnRange());
            for (int i=2;i<6;i++) {
                spawnRange.set(i, (int) timeToDistance(spawnRange.get(i) * 1000));
            }

            if (AirshipGame.DEBUG) {
                Log.d("Level", "Enemy spawner added to level: " + property.getName());

            }

            sm.addSpawner(new BodySpawner(Enemy.class, property.getName(), spawnRange));
        }

        // This method IS SO COOL AND ADDICTED TO METH!!!
        // because method sounds like meth head get it??
    }

    /**
     * Calculates the distance that will be travelled
     * over a certain amount of time based on level speed.
     * @param time The time in milliseconds
     * @return distance travelled after a certain amount of time.
     */
    public double timeToDistance(float time)
    {
        if (getLevelSpeed() == 0) {
            return Double.POSITIVE_INFINITY;
        }

        return time/1000 * (AndroidFastRenderView.UPS * getLevelSpeed());
    }
    
    public String getLeaderboardID()
    {
    	return properties.getLeaderboardID();
    }
    
    public double getLevelSpeed()
    {
        return speed;
    }

    private void calculateLevelSpeed()
    {
        double progress = (double)currentLevel / (double)maxLevel;
        progress = progress > 1 && maxLevelCap ? 1 : progress;
        speed = (endSpeed - startSpeed) * progress + startSpeed;
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
	public void dispose() { music.stop(); }
	
	public void restart()
	{
		generate(properties);
	}
	
    @Override
    public void pause()
    {
        music.pause();
        if (state != GameState.OVER) {
            state = GameState.PAUSED;
        }
    }

    @Override
    public void resume()
    {
    	if (AirshipGame.muted) {
			music.pause();
		} else {
			music.play();
		}
    }

    @Override
    public void backButton()
    {
    	// im a shit joke maker. 
    	// What did the guy with diarrhea say to the guy without a home?
    	// "Hey man, at least you have a good, solid stool."
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED && options.isEnabled()) {
            options.setEnabled(false);
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        } else if (state == GameState.OVER) {
        	game.setScreen(new Menu(game));
        }
    }

    private void checkDifficultyLevel()
    {
        // Once you've touch somebody's heart, you'll be as cool as
        // the rest of the cold hearted murderers out there
        int level = (int) ((elapsedTime / timeToMaxLevel) * maxLevel);
        level = level > maxLevel && maxLevelCap ? maxLevel : level;
        
        if (level > currentLevel) {
            currentLevel = level;
            onDifficultyLevelChange();
        }
    }

    private void onDifficultyLevelChange()
    {
        calculateLevelSpeed();
        for (BodySpawner spawner : sm.getSpawners()) {
            spawner.updateLevel(currentLevel);
        }
    }

    private void buildEndPopup(int score, DeathReason deathReason)
    {
        endPopup = new Popup(game);
        
        
        endPopup.addImage("END/ENDING", Horizontal.CENTER);
        endPopup.marginTop = Assets.getImage("END/ENDING").getHeight() + endPopup.topBorder.getHeight();
        
        endPopup.addHeightMargin(endPopup.topBorder.getHeight() * 2 + 2);
        
        String deathMessageImage = "END/CRASHED";
        
        switch (deathReason) {
        case CRASH:
        	deathMessageImage = "END/CRASHED";
        	break;
        case LACK_OF_WATER:
        	deathMessageImage = "END/WATER";
        	break;
        }
        
        endPopup.addImage(deathMessageImage, Horizontal.CENTER);
        endPopup.addHeightMargin(5);
        endPopup.addNumericImage(score);
        endPopup.addHeightMargin(10);
        
        endPopup.addButton(leaderboardsListener, "END/LEADERBOARDS");
        endPopup.addHeightMargin(3);
        endPopup.addButton(restartListener, "END/RETRY");
        endPopup.addHeightMargin(3);
        endPopup.addButton(hangarListener, "END/HANGAR");
        endPopup.addHeightMargin(3);
        
        endPopup.build();
        
        Graphics g = game.getGraphics();
        if (includeCloudAtEnd && endPopup.getHeight() > g.getHeight()) {
        	includeCloudAtEnd = false;
        	buildEndPopup(score, deathReason);
        }
    }

	public void setBackgroundImage(String image) 
	{
        //Log.d("Level", image);
		backgroundImage = Assets.getImage(image);
		backgroundHeight = backgroundImage.getHeight();
	}

    public void setMusic(String musicName)
    {

    	Music newMusic = Assets.getMusic(musicName);
    	if (this.music == null || (this.music != newMusic && !this.music.isPlaying())) {
    		this.music = newMusic;
    		this.music.setLooping(true);
            this.music.seekBegin();
    	}

        if (this.music != null && this.music.isPlaying()) {
            this.music.stop(); 
        }
        
        if (AirshipGame.muted) {
			this.music.pause();
		} else if (!this.music.isPlaying()) {
			this.music.play();
		}
    }

    public void end(DeathReason deathReason)
    {
        buildEndPopup(bm.getShip().getPlayer().getCurrency(), deathReason);
        state = GameState.OVER;
        endPopup.setEnabled(true);
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

    private void updateSpawners()
    {
        // Spawns objects from LevelSpawningManager. Iterates through all spawners,
        // checks if spawner can spawn, Instantiates however that object spawns and
        // adds them to LevelBodyManager.
        for (BodySpawner spawner : sm.getSpawners()) {
            spawner.updateDistance(currentLevel, getLevelSpeed());

            if (spawner.canSpawn( currentLevel, 1.0 ) && isSpawning) {
                List<GameBody> bodyList = spawner.trySpawnObjects(this);

                for (GameBody body : bodyList) {
                    getBodyManager().addBody(body);
                }

                spawner.resetSpawnDistance();
            }
        }
    }

    @Override
    public void paint(float deltaTime)
    {
        switch(state) {
            case RUNNING:
                paintRunning(deltaTime);
                break;
            case PAUSED:
                paintRunning(deltaTime);
                paintPaused(deltaTime);
                break;
            case OVER:
                paintRunning(deltaTime);
                paintOver(deltaTime);
                break;
        }

    }

    private void paintOver(float deltaTime)
    {
        endPopup.paint(deltaTime);
    }

    private void paintPaused(float deltaTime)
    {
        for(Button button: pauseButtons) {
            button.onPaint(deltaTime);
        }
        options.paint(deltaTime);
    }

    private void paintRunning(float deltaTime)
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
        
        bm.getShip().getPlayer().onPaint(deltaTime);

    }

	@Override
	public void update(float deltaTime)
	{
		
        // Level handles this as it needs to be precise.
        // It takes too long for the time to get from AndroidFastRenderView
        // to this method. This causes jerky movements and overall a crappy game.
        long now = System.nanoTime();
        double deltaSeconds = (now - lastUpdate)/1000000000.0;
        lastUpdate = now;

		switch(state) {
		case RUNNING:
			updateRunning((double)deltaSeconds);
			break;
		case PAUSED:
			updatePaused((double)deltaTime);
			break;
		case OVER:
			updateOver((double)deltaTime);
			break;
		}
	}

	public void updateOver(double deltaTime)
	{
        endPopup.update(deltaTime);
        
        if (!endPopup.isEnabled()) {
        	state = GameState.PAUSED;
        }
	}

	private void updatePaused(double deltaTime)
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

	private void updateRunning(double deltaSeconds)
	{
        elapsedTime += deltaSeconds;

        // Checks difficulty level and updates if needed.
        checkDifficultyLevel();

        //updates all bodies in body manager (ship, enemies, items)
        bm.onUpdate(deltaSeconds);
        bm.offsetBodies(new Vector2d( 0, deltaSeconds * getLevelSpeed() ));

        // Updates menu buttons (options gear)
		for (Button button : buttons) {
			button.onUpdate(deltaSeconds);
		}

		// Updates background position
		backgroundPos += deltaSeconds * getLevelSpeed();
		backgroundPos = backgroundPos > backgroundHeight ? 0 : backgroundPos;

        updateSpawners();
	}

    private void generateListeners()
    {
        resumeListener = new ButtonClickListener() {
            @Override
            public void onUp() {
                state = GameState.RUNNING;
            }
        };
        settingsListener = new ButtonClickListener() {
            @Override
            public void onUp() {
                options.setEnabled(true);
            }
        };
        pauseListener = new ButtonClickListener() {
            @Override
            public void onUp() {
                toggleLevelPauseState();
            }
        };
        hangarListener = new ButtonClickListener() {
            @Override
            public void onUp() { game.setScreen(new Menu(game, (Level)game.getCurrentScreen())); }
        };
        muteListener = new ButtonClickListener() {
            @Override
            public void onUp() { 
            	if (music.isPlaying()) {
            		AirshipGame.muted = true;
            		music.pause();
            	} else {
            		AirshipGame.muted = false;
            		music.play();
            	}
            	
            }
        };
        leaderboardsListener = new ButtonClickListener() {
            @Override
            public void onUp() { 
            	game.loadBoard(getLeaderboardID());
            }
        };
        restartListener = new ButtonClickListener() {
            @Override
            public void onUp() { 
            	generate(properties);
            	state = GameState.RUNNING;
            }
        };
    }
}
