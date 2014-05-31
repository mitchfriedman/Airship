package com.mitch.framework;

import java.util.ArrayList;
import java.util.HashMap;

import com.mitch.flyship.Assets;
import com.mitch.flyship.objects.Button;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.Input;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class Popup {
	
	Image background;
	Vector2d position;
	AndroidGame game;
	boolean enabled;
	
	private static final int TOUCH_OFFSET = 5;
	
	HashMap<String, Vector2d> images;
	ArrayList<Button> buttons = new ArrayList<Button>();
	
	
	
	public Popup(AndroidGame game, String name) {
		/* At the time of writing this code, I am the only one who knows what I am doing.
		 * By the time I read this again, nobody will know what I have done. What was I thinking?
		 * Sometimes you just need to sit back and smoke a joint, let it come to you later on. */
		this.game = game;
		background = Assets.getImage(name);
		Graphics g = game.getGraphics();
		position = new Vector2d((g.getWidth()-background.getWidth())/2, (g.getHeight()-background.getHeight())/2);
		
		images = new HashMap<String, Vector2d>();
		buttons = new ArrayList<Button>();
	}
	
	public void update(float deltaTime) {
		
		Input input = game.getInput();
		
		for(Button button : buttons) {
			button.onPaint(deltaTime);
		}
		
		if (input.isTouchDown(0)) {
			if(touchedOutside(input)) {
				setEnabled(false);
			}
		}
	}
	private boolean touchedOutside(Input input) {
		
		
		return (input.getTouchY(0) < position.y - TOUCH_OFFSET|| input.getTouchY(0) > position.y + background.getHeight() + TOUCH_OFFSET||
				input.getTouchX(0) < position.x - TOUCH_OFFSET || input.getTouchX(0) > position.x + background.getWidth() + TOUCH_OFFSET);
	}
	
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		
		if(enabled) {
			g.drawImage(background, position);
			
			for (HashMap.Entry<String, Vector2d> image : images.entrySet()) {
			    String name = image.getKey();
			    Vector2d position = image.getValue();
			    
			    Image temp = Assets.getImage(name);
			    g.drawImage(temp, position);
			}
			for(Button button : buttons) {
				button.onPaint(deltaTime);
			}
		}
		
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean getEnabled() {
		return this.enabled;
	}
}
