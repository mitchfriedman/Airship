package com.mitch.flyship.screens;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.ButtonClickListener;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.objects.Button;
import com.mitch.flyship.objects.Platform;
import com.mitch.flyship.objects.Terrain;
import com.mitch.framework.Graphics;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;

public class Menu extends Screen {
	
	List<GameBody> bodies = new ArrayList<GameBody>();
	
	ButtonClickListener gearListener = new ButtonClickListener() {
		@Override
		public void onUp() { 
			Log.d("GEAR", "UP");
		}
		@Override
		public void onDown() { 
			Log.d("GEAR", "DOWN");
		}
		@Override
		public void onCancel() { 
			Log.d("GEAR", "CANCEL");
		}
	};		
	ButtonClickListener endlessListener = new ButtonClickListener() {
		@Override
		public void onUp() { 
			game.setScreen(new Level(game));
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
	
	public Menu(AirshipGame game)
	{
		super(game);
		
		Graphics g = game.getGraphics();
		Assets.getMusic("blue").setLooping(true);
		bodies.add(new Terrain(game, "Menu/terrain"));
		bodies.add(new Platform(game, "Menu/platform"));
		
		
		Align alignment;
		Vector2d position;
		
		alignment = new Align(Align.Vertical.TOP, Align.Horizontal.RIGHT);
		position = new Vector2d(g.getWidth()-8, 8);
		bodies.add(new Button(game, "GUI/Gear", alignment, position, gearListener));
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.LEFT);
		position = new Vector2d(0, g.getHeight());
		bodies.add(new Button(game, "Menu/Buttons/Missions", alignment, position, missionsListener));
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.CENTER);
		position = new Vector2d(g.getWidth()/2, g.getHeight());
		bodies.add(new Button(game, "Menu/Buttons/Endless", alignment, position, endlessListener));
		
		alignment = new Align(Align.Vertical.BOTTOM, Align.Horizontal.RIGHT);
		position = new Vector2d(g.getWidth(), g.getHeight());
		bodies.add(new Button(game, "Menu/Buttons/Shop", alignment, position, shopListener));
	}
	
	@Override
	public void update(float deltaTime) 
	{
		for (GameBody body : bodies) {
			body.onUpdate(deltaTime);
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
		Assets.getMusic("blue").pause();
	}

	@Override
	public void resume() {
		Assets.getMusic("blue").play();
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void backButton() {
		
	}

}
