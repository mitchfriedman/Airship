package com.mitch.flyship.Enemy.Components;

import java.util.ArrayList;
import java.util.List;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Assets;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Coin;
import com.mitch.flyship.objects.Ship;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

/**
 * A blank Enemy Component.
 */
public class CoinStealer extends EnemyComponent {
	private final static String FONT = "FONT/COIN/";
	private final static int N_DIGITS = 3;
	private final static List<Image> fontImages = new ArrayList<Image>();
	
	private final static double ATTRACTION_RANGE = 105;
	private final static double COLLECITON_RANGE = 4;
	private final static double COIN_STEALING_INTERVAL = 0.1;
	private double attractionSpeed = 0;
	
	private Vector2d counterOffset = new Vector2d(0,0);
	
	private boolean enabled = true;
	private int fontWidth;
	private Vector2d[] numberPositions = new Vector2d[N_DIGITS];
	private Image[] numberImages = new Image[N_DIGITS];
	private int nCoins = 0;
	private double timeSinceLastSteal = 0;
	
    public CoinStealer() {}

    // DEBUGGING: Did you remember to clone the elements?
    public CoinStealer(XmlResourceParser parser) //xml stuff here
    { 
    	this(); 
    	counterOffset.x = parser.getAttributeFloatValue(null, "offsetX", 0);
    	counterOffset.y = parser.getAttributeFloatValue(null, "offsetY", 0);
    }
    
    @Override
    public void onComponentAdded() {
    	super.onComponentAdded();
    	if (fontImages.size() == 0) {
    		for (int i = 0; i < 10; i++) {
        		fontImages.add(Assets.getImage(FONT + i));
        	}
    	}
    	
    	fontWidth = fontImages.get(0).getWidth();
    	
    	for( int i = 0; i < N_DIGITS; i++ ) {
    		numberPositions[i] = counterOffset
    				.add(new Vector2d(fontWidth*i,0))
    				.add(new Vector2d(i*1,0));
    	}
		
    	attractionSpeed = enemy.getLevel().getBodyManager().getShip().coinAttractionSpeed;
    }
    
    @Override
    public void onObjectCreationCompletion() {
    	super.onObjectCreationCompletion();
    	updateCoinCounter();
    }
    
    @Override
    public void onHit(Ship ship) {
    	super.onHit(ship);
    	enabled = false;
    }
    
    @Override
    public void onUpdate(double deltaTime) {
    	super.onUpdate(deltaTime);
    	updateCoinCounter();
		timeSinceLastSteal += deltaTime;
    	if (canStealCoins() && enabled) {
    		stealCoin();
    		timeSinceLastSteal = 0;
    	}
    	
    	if (enabled) {
        	attractCoins();
    	}
    	
    }
    
    private boolean canStealCoins()
    {
    	Ship ship = enemy.getLevel().getBodyManager().getShip();
    	Vector2d shipCenter = ship.getPos().add( ship.getSize().divide(2) );
    	Vector2d enemyCenter = enemy.getPos().add( enemy.getSize().divide(2) );
    	
    	boolean withinRange = shipCenter.getDistance(enemyCenter) < ATTRACTION_RANGE;
    	boolean durationElapsed = timeSinceLastSteal > COIN_STEALING_INTERVAL;
    	return withinRange && durationElapsed;
    }
    
    private void stealCoin()
    {
    	Ship ship = enemy.getLevel().getBodyManager().getShip();
    	ship.dropCoin(Coin.CoinType.ONE);
    }
    
    private void attractCoins()
    {
    	List<Coin> bodies = enemy.getLevel().getBodyManager().getBodiesByClass(Coin.class);
        for (Coin coin : bodies) {
            Vector2d center = enemy.getPos().add( enemy.getSize().divide(2) );
            double distance = coin.getPos().getDistance(center);
            
            if ( distance < COLLECITON_RANGE ) {
            	nCoins += coin.value;
            	enemy.getLevel().getBodyManager().removeBody(coin);
            }
            else if (distance < ATTRACTION_RANGE) {
                Vector2d direction = center.subtract(coin.getPos());
                double distanceDivisor = direction.getLength() / ATTRACTION_RANGE;
                double length = attractionSpeed - distanceDivisor * attractionSpeed;
                Vector2d coinPos = coin.getPos().add(direction.normalize().scale(length));
                coin.setPos(coinPos);
            }
        }
    }
    
    private void updateCoinCounter()
    {
    	for (int i = 0; i < N_DIGITS; i++) {
    		int place = (int) Math.pow(10, N_DIGITS-i);
    		int numberAtPosition = (int) Math.floor(nCoins % place / (place / 10));
    		numberImages[i] = fontImages.get(numberAtPosition);
    	}
    }
    
    @Override
    public void onPaint(float deltaTime) {
    	super.onPaint(deltaTime);
    	
    	if (enabled) {
        	Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
    		for (int i = 0; i < N_DIGITS; i++) {
        		g.drawImage(numberImages[i], enemy.getPos().add(numberPositions[i]));
        	}
    	}
    }
    

    @Override
    public EnemyComponent clone() {
        CoinStealer component = new CoinStealer();
        component.counterOffset = counterOffset;
        return component;
    }
}
