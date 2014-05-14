package com.mitch.flyship.objects;

import com.mitch.flyship.GameBody;
import com.mitch.flyship.Player;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.containers.Vector2d;

public class Ship extends GameBody {
	
	Player player;
	Level level;
	ShipParams params;
	
	public Ship(Level level, Player player, ShipParams params, Vector2d pos)
	{
		super(level.getAirshipGame(), "PlayerShip", pos);
		this.player = player;
		this.level = level;
		this.params = params;
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		velocity = player.getInput_Speed();
		setPos(getPos().add(velocity));
		
		
		//TODO: set ship image based on velocity
	}

	@Override
	public void onPaint(float deltaTime) {

	}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}

}
