package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;

public class Platform extends GameBody {
	
	Image image;

	public Platform(AirshipGame game, String name, Rect bounds) {
		super(game, name, bounds);
		this.image = Assets.getImage(name);
	}
	public Image getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = Assets.getImage(image);
	}
	@Override
	public void onUpdate(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaint(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
		//g.drawImage(getImage(), getBounds().getRealPosition(), getBounds().getRealSize());
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
}
