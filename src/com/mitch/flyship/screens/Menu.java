package com.mitch.flyship.screens;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.LevelProperties;
import com.mitch.flyship.objects.Button;
import com.mitch.flyship.objects.Platform;
import com.mitch.flyship.objects.Terrain;
import com.mitch.framework.Graphics;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;

public class Menu extends Screen {

    private static final int ANIMATION_DURATION = 5000;

	List<GameBody> bodies = new ArrayList<GameBody>();
    List<Vector2d> bodiesStartPos = new ArrayList<Vector2d>();
    private Platform platform;

    private int timeTostart;
    private int elapsedTime;

	ButtonClickListener gearListener = new ButtonClickListener() {
		@Override
		public void onUp() { }
		@Override
		public void onDown() { }
		@Override
		public void onCancel() { }
	};
	ButtonClickListener endlessListener = new ButtonClickListener() {
		@Override
		public void onUp() {
            //game.setScreen(new Level(game, LevelProperties.getLevel("demo")));
            platform.startGame();

		}
		@Override
		public void onDown() { }
		@Override
		public void onCancel() { }
	};
	ButtonClickListener missionsListener = new ButtonClickListener () {
		@Override
		public void onUp() { game.setScreen(new Level(game, LevelProperties.getLevel("ocean"))); }
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
	ButtonClickListener instructionsListener = new ButtonClickListener() {
		@Override
		public void onUp() { }
		public void onDown() { }
		public void onCancel() { }
	};

	public Menu(AirshipGame game)
	{
		super(game);
		
		Graphics g = game.getGraphics();
		Assets.getMusic("wind").setLooping(true);
		Assets.getMusic("wind").setVolume(0.1f);
		platform = new Platform(game, "Menu/platform");
		bodies.add(new Terrain(game, "Menu/terrain"));
		bodies.add(platform);

        elapsedTime = 0;
        timeTostart = platform.getAnimationDuration();
		
		Align alignment;
		Vector2d position;
		
		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
		position = new Vector2d(g.getWidth()-8, 8);
		bodies.add(new Button(game, "GUI/Gear", alignment, position, gearListener));
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.LEFT);
		position = new Vector2d(0, g.getHeight()-3);
		bodies.add(new Button(game, "Menu/Buttons/Missions", alignment, position, missionsListener));
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.CENTER);
		position = new Vector2d(g.getWidth()/2, g.getHeight()-3);
		bodies.add(new Button(game, "Menu/Buttons/Endless", alignment, position, endlessListener));
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.RIGHT);
		position = new Vector2d(g.getWidth(), g.getHeight()-3);
		bodies.add(new Button(game, "Menu/Buttons/Shop", alignment, position, shopListener));

        for(GameBody body: bodies) {
            bodiesStartPos.add(body.getPos());
        }
		
	}

	@Override
	public void update(float deltaTime) 
	{
		for (GameBody body : bodies) {
			body.onUpdate(deltaTime);
		}
        if(platform.getStartingGame()) {
            clearButtonsToStart();
        }
        elapsedTime += deltaTime;
        if(platform.readyToStart()) {
            //if(elapsedTime >= timeTostart) {
            game.setScreen(new Level(game, LevelProperties.getLevel("demo")));
            //}
            elapsedTime += deltaTime;
        }
	}

	@Override
	public void paint(float deltaTime)
	{	
		for (GameBody body : bodies) {
			body.onPaint(deltaTime);
		}
		
	}
	
	@Override
	public void pause() 
	{
		Assets.getMusic("wind").pause();
	}

	@Override
	public void resume() {
		Assets.getMusic("wind").play();
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void backButton() {
		
	}

    private void clearButtonsToStart() {
        for(int i=0;i<bodies.size();i++) {
            float endPosY = game.getGraphics().getHeight();
            float positionDifference = endPosY - (float)bodiesStartPos.get(i).y;
            float tweenPositionRatio = elapsedTime / ANIMATION_DURATION;
            GameBody body = bodies.get(i);
            body.setPos(new Vector2d(body.getPos().x, body.getPos().y + positionDifference * tweenPositionRatio));
        }
    }


}
