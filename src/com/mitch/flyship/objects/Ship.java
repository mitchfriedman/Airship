package com.mitch.flyship.objects;

import java.util.List;

import com.mitch.flyship.AnimationImage;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.Player;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class Ship extends GameBody {
	
	Player player;
	Level level;
	
	int coinCollectionRange;
	int coinAttractionRange;
	double coinAttractionSpeed;
	boolean imageReversed = false;
	AnimationImage propellerAnim;
	Vector2d propellerPos;
	Image tilt0;
	Image tilt1;
	Image tilt2;
	Image image;
	
	
	public Ship(Level level, Player player, ShipParams params, Vector2d pos)
	{
		super(level.getAirshipGame(), "PlayerShip", pos);
		super.affectedByLevelSpeed = false;
		this.player = player;
		this.level = level;
		setDepth(150);
		setParams(params);
		image = tilt0;
		setSize(image.getSize());
	}
	
	void setParams(ShipParams params)
	{
		tilt0 = params.tilt0;
		tilt1 = params.tilt1;
		tilt2 = params.tilt2;
		propellerAnim = new AnimationImage(params.animation_fps);
		propellerAnim.setFrames(params.animationRects);
		propellerPos = params.propellerPos;
		
		coinAttractionRange = params.coinAttractionRange;
		coinCollectionRange = params.coinCollectionRange;
		coinAttractionSpeed = params.coinAttractionSpeed;
		
		
		super.collisionOffset = params.collisionOffset;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public void onUpdate(float deltaTime) 
	{
		
		propellerAnim.updateTime(deltaTime);
		
		Vector2d velocity = player.getInput_Speed();
		velocity.y += level.getSpeed();
		
		addVelocityWithBoundsCheck(velocity);
		setImageFromVelocity(velocity);
		checkForCollisions();
		attractNearbyCoins();
		player.onUpdate(deltaTime);
	}

	@Override
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos(), imageReversed, false);
		g.drawImage(propellerAnim.getFrame().image, getPos().add(propellerPos), 
				propellerAnim.getFrame().reverseX, propellerAnim.getFrame().reverseY);
		player.onPaint(deltaTime);
	}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}
	
	
	void addVelocityWithBoundsCheck(Vector2d velocity)
	{
		Vector2d newPos = getPos().add(velocity);
		
		// Checks x bounds.
		if (newPos.x > player.getShipBounds().x && newPos.x < player.getShipBounds().width - getSize().x) {
			setPos(new Vector2d(newPos.x, getPos().y));
		}
		
		// Checks y bounds
		if (newPos.y > player.getShipBounds().y && newPos.y < player.getShipBounds().height - getSize().y) {
			setPos(new Vector2d(getPos().x, newPos.y));
		}
	}
	
	void setImageFromVelocity(Vector2d velocity)
	{
		imageReversed = velocity.x < 0;
		if (Math.abs(velocity.x) >= player.MAX_SPEED_HORIZONTAL) {
			image = tilt2;
		}
		else if (Math.abs(velocity.x) > player.MAX_SPEED_HORIZONTAL/3) {
			image = tilt1;
		}
		else {
			image = tilt0;
			imageReversed = false;
		}
	}
	
	void checkForCollisions()
	{
		List<Enemy> enemies = level.getBodyManager().getBodiesBySuperClass(Enemy.class);
		
		for (Enemy enemy : enemies) {
			if (Rect.rectCollides(enemy.getCollisionBounds(), getCollisionBounds())) {
				enemy.onHit();
				player.applyDamage(enemy.damage);
			}
		}
	}
	
	void attractNearbyCoins()
	{
		List<Coin> coins = level.getBodyManager().getBodiesByClass(Coin.class);
		for (Coin coin : coins) {
			
			Vector2d shipCenter = getPos().add(getSize().scale(0.5));
			double distance = coin.getPos().getDistance(shipCenter);
			if ( distance < coinCollectionRange ) {
				player.addCurrency(coin.value);
				level.getBodyManager().removeBody(coin);
			}
			else if ( distance < coinAttractionRange ) {
				Vector2d direction = shipCenter.subtract(coin.getPos());
				double distanceDivisor = direction.getLength() / coinAttractionRange;
				double length = coinAttractionSpeed - distanceDivisor * coinAttractionSpeed;
				Vector2d coinPos = coin.getPos().add(direction.normalize().scale(length));
				coin.setPos(coinPos);
			}
		}
	}
}
