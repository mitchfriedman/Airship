package com.mitch.framework;

import java.util.List;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.GameBody;
import com.mitch.framework.containers.Align;
import com.mitch.framework.containers.Vector2d;
public abstract class Popup {
	
	int height;
	int width;
	Vector2d position;
	Align align; 
	List<GameBody> bodies;
	AirshipGame game;
	
	public Popup(AirshipGame game, Align align) {
		setPosition(align);
		this.game = game;
	}
	
	public Popup(Vector2d position, Align align) {
		this.position = position;
		this.align = align;
	}
	
	public void onPaint() {
		Graphics g = game.getGraphics();
		
	}
	
	public void onUpdate() {
		
	}
	
	private void setPosition(Align align) {
		
	}
}
