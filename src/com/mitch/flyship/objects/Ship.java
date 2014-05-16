package com.mitch.flyship.objects;

import android.util.Log;

import com.mitch.flyship.GameBody;
import com.mitch.flyship.Player;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Ship extends GameBody {
	
	Player player;
	Level level;
	ShipParams params;
	Image image;
	
	public Ship(Level level, Player player, ShipParams params, Vector2d pos)
	{
		super(level.getAirshipGame(), "PlayerShip", pos);
		this.player = player;
		this.level = level;
		this.params = params;
		image = params.tilt0;
		setSize(image.getSize());
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		velocity = player.getInput_Speed();
		setPos(getPos().add(velocity));
		if (player.getInput_ShootRight()) {
			player.centerOrientation();
		}
		
		Log.d("Orientation Offset", " " + Math.round(player.getCenteredOrientation().x) + " " +  Math.round(player.getCenteredOrientation().y));
	}

	@Override
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos());
	}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}

}
