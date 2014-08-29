package com.mitch.flyship.objects;

import java.util.List;

import android.graphics.Color;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.AnimationImage;
import com.mitch.flyship.AttractionListener;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.Player;
import com.mitch.flyship.ShipParams;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class Ship extends GameBody {
	
	static final double FLASH_TIME = 3.5;
	static final double FLASH_SPEED = 0.2;
	
	Player player;
	Level level;
	
	int waterCollectionRange;
	int waterAttractionRange;
	double waterAttractionSpeed;
	
	int coinCollectionRange;
	int coinAttractionRange;
	double coinAttractionSpeed;
	
	boolean imageReversed = false;
    final float maxPropellerFPS;

	AnimationImage propellerAnim;
	Vector2d propellerPos;
	Image tilt0;
	Image tilt1;
	Image tilt2;
	Image image;
	
	double timeSinceFlashBegin = 0;
	boolean flashing = false;
	boolean drawing = true;
	boolean collectingCoins = true;
	
	
	public Ship(Level level, Player player, ShipParams params, Vector2d pos)
	{
		super(level.getAirshipGame(), "PlayerShip", pos);
		super.affectedByLevelSpeed = false;
        this.maxPropellerFPS = params.animation_fps;
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
		coinAttractionSpeed = params.coinAttractionSpeed;
		coinCollectionRange = params.coinCollectionRange;
		
		waterAttractionRange = params.waterAttractionRange;
		waterAttractionSpeed = params.waterAttractionSpeed;
		waterCollectionRange = params.waterCollectionRange;
		
		super.collisionOffset = params.collisionOffset;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	@Override
	public void onUpdate(double deltaTime)
	{
		propellerAnim.updateTime(deltaTime);

        Vector2d velocity = player.getInput_Speed(deltaTime);

        propellerAnim.resume();
        if (velocity.y != 0) {
            double maxSpeed = velocity.y > 0 ? player.getMaxSpeedDown(deltaTime) : player.getMaxSpeedUp(deltaTime);
            float animationSpeedRatio = (float) (-velocity.y / maxSpeed);
            propellerAnim.setSpeed( animationSpeedRatio * maxPropellerFPS );

        } else {
            propellerAnim.pause();
        }

        //velocity = velocity.add( new Vector2d(0, level.getLevelSpeed()) );

		addVelocityWithBoundsCheck(velocity);
		setImageFromVelocity(velocity, deltaTime);
		checkForCollisions();
        attractObjects();

		player.onUpdate(deltaTime);
		
		timeSinceFlashBegin += deltaTime;
		if (flashing && timeSinceFlashBegin < FLASH_TIME) {
			drawing = (int)(timeSinceFlashBegin / FLASH_SPEED % 2) == 1;
			player.setInvulnerable(true);
			
		} else {
			player.setInvulnerable(false);
			flashing = false;
			drawing = true;
			collectingCoins = true;
		}
	}

	@Override
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		if (drawing) {
			g.drawImage(image, getPos(), imageReversed, false);
			g.drawImage(propellerAnim.getFrame().image, getPos().add(propellerPos), 
					propellerAnim.getFrame().reverseX, propellerAnim.getFrame().reverseY);

	        if (AirshipGame.SHOW_COLLIDER_BOXES) {
	            game.getGraphics().drawRect(this.getCollisionBounds(), Color.BLUE);
	        }
		}
		
	}

	@Override
	public void onPause() {}

	@Override
	public void onResume() {}
	
	public void flash()
	{
		flashing = true;
		collectingCoins = false;
		timeSinceFlashBegin = 0;
	}
	
	
	void addVelocityWithBoundsCheck(Vector2d velocity)
	{
		Vector2d newPos = getPos().add(velocity);
		
		// Checks x bounds.
		if (newPos.x > player.getShipBounds().x &&
                newPos.x < player.getShipBounds().width - getSize().x) {
			setPos(new Vector2d(newPos.x, getPos().y));
		}
		
		// Checks y bounds
		if (newPos.y > player.getShipBounds().y &&
                newPos.y < player.getShipBounds().height - getSize().y) {
			setPos(new Vector2d(getPos().x, newPos.y));
		}
	}
	
	void setImageFromVelocity(Vector2d velocity, double deltaTime)
	{
		imageReversed = velocity.x < 0;
		if (Math.abs(velocity.x) >= player.getMaxSpeedHorizontal(deltaTime)) {
			image = tilt2;
		}
		else if (Math.abs(velocity.x) > player.getMaxSpeedHorizontal(deltaTime)/3) {
			image = tilt1;
		}
		else {
			image = tilt0;
			imageReversed = false;
		}
	}
	
	void checkForCollisions()
	{
		for ( Enemy enemy : level.getBodyManager().getEnemies() ) {
			if (Rect.rectCollides(enemy.getCollisionBounds(), getCollisionBounds())
                    && enemy.isColliding()
                    && !flashing) {
				enemy.onHit(this);
				player.applyDamage(enemy.getDamage());
			}
		}
	}

    private void attractObjects()
    {
    	if (collectingCoins) {
	    		attractNearbyObject(Coin.class,
	                coinCollectionRange, coinAttractionRange, coinAttractionSpeed,
	                new AttractionListener() {
	                    @Override
	                    public void onCollection(GameBody body) {
	                        player.addCurrency( ((Coin)body).value );
	                        level.getBodyManager().removeBody(body);
	                    }
	                    @Override
	                    public void onAttraction(GameBody body) {}
	                });
    	}
       

        attractNearbyObject(Water.class,
                waterCollectionRange, waterAttractionRange, waterAttractionSpeed,
                new AttractionListener() {
                    @Override
                    public void onCollection(GameBody body) {
                        player.addWater();
                        level.getBodyManager().removeBody(body);
                    }
                    @Override
                    public void onAttraction(GameBody body) {}
                });
    }

    private<T extends GameBody> void attractNearbyObject(Class<T> bodyClass, double collectionRange, double attractionRange,
                             double attractionSpeed, AttractionListener listener)
    {
        List<T> bodies = level.getBodyManager().getBodiesByClass(bodyClass);
        for (T iBody : bodies) {
            Vector2d shipCenter = getPos().add( getSize().scale(0.5) );
            double distance = iBody.getPos().getDistance(shipCenter);

            if ( distance < collectionRange ) {
                listener.onCollection(iBody);
            }
            else if (distance < attractionRange) {
                Vector2d direction = shipCenter.subtract(iBody.getPos());
                double distanceDivisor = direction.getLength() / attractionRange;
                double length = attractionSpeed - distanceDivisor * attractionSpeed;
                Vector2d coinPos = iBody.getPos().add(direction.normalize().scale(length));
                iBody.setPos(coinPos);
                listener.onAttraction(iBody);
            }
        }
    }
}
