package com.mitch.flyship.screens;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.LevelProperties;
import com.mitch.flyship.Popup;
import com.mitch.flyship.objects.Button;
import com.mitch.flyship.objects.Platform;
import com.mitch.flyship.objects.Terrain;
import com.mitch.framework.Graphics;
import com.mitch.framework.Music;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;

public class Menu extends Screen {

	List<GameBody> bodies = new ArrayList<GameBody>();
	List<Button> buttons = new ArrayList<Button>();
	
	Music music;
	Level level;
	Vector2d platformPos;
	
	ButtonClickListener rankingsListener = new ButtonClickListener() {
        @Override
        public void onUp() {
        	game.loadBoards();
        	
        }
    };
    ButtonClickListener settingsListener = new ButtonClickListener() {
        @Override
        public void onUp() {
        	Popup.settings.setEnabled(true);
        }
    };
	ButtonClickListener muteListener = new ButtonClickListener() {
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
    ButtonClickListener embarkListener = new ButtonClickListener() {
        @Override
        public void onUp() {
            game.setScreen(new Level(game, LevelProperties.getLevel("demo")));
        }
    };
    ButtonClickListener resumeListener = new ButtonClickListener() {
        @Override
        public void onUp() {
        	game.setScreen(level);
        }
    };
    
    public Menu(AirshipGame game, Level level)
    {
    	this(game);
    	this.level = level;
    	buttons.clear();
    	generateButtons();
    }
    
	public Menu(AirshipGame game)
	{
		super(game);
		
		music = Assets.getMusic("wind");
		music.setLooping(true);
		music.setVolume(0.25f);
		
		bodies.add(new Terrain(game, "Menu/terrain"));
		Platform platform = new Platform(game, "Menu/platform");
		platformPos = platform.getPos();
		bodies.add(platform);
		
        Popup.buildSettingsPopup(game);
		generateButtons();
	}
	
	private void generateButtons()
	{
		Align alignment;
		Vector2d position;
		
		Graphics g = game.getGraphics();
		
		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.LEFT);
		position = new Vector2d(118, 15).add(platformPos);
		if (level != null) {
			buttons.add(new Button(game, "Buttons/resume", alignment, position, resumeListener));
		} else {
			buttons.add(new Button(game, "Buttons/embark", alignment, position, embarkListener));
		}
		
		position = new Vector2d(118, 61).add(platformPos);
		buttons.add(new Button(game, "Buttons/rankings", alignment, position, rankingsListener));

		position = new Vector2d(118, 106).add(platformPos);
		buttons.add(new Button(game, "Buttons/settings", alignment, position, settingsListener));
		
		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
        position = new Vector2d(g.getWidth()-8, 8);
        Button button = new Button(game, "GUI/mute", alignment, position, muteListener);
        button.setToggled(AirshipGame.muted);
        buttons.add(button);
	}
	
	@Override
	public void update(float deltaTime) 
	{
		
		Popup.settings.update(deltaTime);
		
		if (!Popup.settings.isEnabled()) {
			for (GameBody body : buttons) {
				body.onUpdate(deltaTime / 1000);
			}
		}
		
		for (GameBody body : bodies) {
			body.onUpdate(deltaTime / 1000);
		}
	}

	@Override
	public void paint(float deltaTime)
	{	
		for (GameBody body : bodies) {
			body.onPaint(deltaTime / 1000);
		}
		
		for (GameBody body : buttons) {
			body.onPaint(deltaTime / 1000);
		}

		Popup.settings.paint(deltaTime);
		
	}
	
	@Override
	public void pause() 
	{
		music.pause();
	}

	@Override
	public void resume() {
		if (AirshipGame.muted) {
			music.pause();
		} else {
			music.play();
		}
	}

	@Override
	public void dispose() {
		
	}
	
	@Override
	public void backButton() {
		if (Popup.settings.isEnabled()) {
			Popup.settings.setEnabled(false);
		}
	}


}
