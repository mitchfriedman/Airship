package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public abstract class Enemy extends GameBody {
	
	Image image;
	private int damage;

	public Enemy(AirshipGame game, Vector2d pos, Vector2d vel) {
		super(game, "Cloud", pos);
		super.velocity = vel;
		
	}
	
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public abstract void onHit();

	@Override
	public void onUpdate(float deltaTime) { 
		setPos(getPos().add(velocity));
	}

	@Override
	public void onPaint(float deltaTime) { 
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos(), getSize());
	}

	@Override
	public void onPause() { }

	@Override
	public void onResume() { }

}
