package com.mitch.framework;

import com.mitch.flyship.AirshipGame;
import com.mitch.framework.Input.TouchEvent;

public abstract class Screen {
    protected final AirshipGame game;

    public Screen(AirshipGame game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void paint(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
    
	public abstract void backButton();
	
	public boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}
}
