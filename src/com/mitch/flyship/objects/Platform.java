package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Platform extends GameBody {
	
	Image image;

	public Platform(AirshipGame game, String name) {
		super(game, name);
		
		this.image = Assets.getImage(name);
		setPos(new Vector2d(0, game.getGraphics().getHeight()-image.getHeight()));
		
	}
	public Image getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = Assets.getImage(image);
	}
	@Override
	public void onUpdate(float deltaTime) {
		
	}

	@Override
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(getImage(), getPos().x, getPos().y);
		g.drawImage(Assets.getImage("Menu/cargo"), g.getWidth()/2+30, getPos().y+60);
		g.drawImage(Assets.getImage("Menu/bottom border"), 0, g.getHeight()-3);
		g.drawImage(Assets.getImage("ship/Interceptor-normal"), g.getWidth()/2-10, getPos().y+25);
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
}
