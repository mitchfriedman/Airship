package com.mitch.flyship.screens;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.LevelProperties;
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
	
	Music music;
	
	ButtonClickListener endlessListener = new ButtonClickListener() {
		@Override
		public void onUp() {
            game.setScreen(new Level(game, LevelProperties.getLevel("demo")));
		}
		@Override
		public void onDown() { }
		@Override
		public void onCancel() { }
	};
	
	ButtonClickListener missionsListener = new ButtonClickListener () {
		@Override
		public void onUp() { }
		@Override
		public void onDown() { }
		@Override
		public void onCancel() { }
	};
	ButtonClickListener shopListener = new ButtonClickListener() {
		@Override
		public void onUp() { }
		@Override
		public void onDown() { }
		@Override
		public void onCancel() { }
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

	public Menu(AirshipGame game)
	{
		super(game);
		
		
		music = Assets.getMusic("wind");
		music.setLooping(true);
		music.setVolume(0.25f);
		
		bodies.add(new Terrain(game, "Menu/terrain"));
		bodies.add(new Platform(game, "Menu/platform"));
		
		Align alignment;
		Vector2d position;
		
		Graphics g = game.getGraphics();
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.LEFT);
		position = new Vector2d(0, g.getHeight()-3);
		bodies.add(new Button(game, "Menu/Buttons/Missions", alignment, position, missionsListener));
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.CENTER);
		position = new Vector2d(g.getWidth()/2, g.getHeight()-3);
		bodies.add(new Button(game, "Menu/Buttons/Endless", alignment, position, endlessListener));
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.RIGHT);
		position = new Vector2d(g.getWidth(), g.getHeight()-3);
		bodies.add(new Button(game, "Menu/Buttons/Shop", alignment, position, shopListener));
		
		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
        position = new Vector2d(g.getWidth()-8, 8);
        Button button = new Button(game, "GUI/mute", alignment, position, muteListener);
        button.setToggled(AirshipGame.muted);
        bodies.add(button);
		
	}

	@Override
	public void update(float deltaTime) 
	{
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
		
	}


}
