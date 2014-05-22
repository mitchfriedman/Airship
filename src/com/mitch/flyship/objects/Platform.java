package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaint(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
		g.drawImage(getImage(), getPos().x, getPos().y);
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
