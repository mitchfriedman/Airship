package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;


public class Platform extends GameBody {
	
	private Image image;

    private static final int ANIMATION_DURATION = 10000;

    private Vector2d cargoPos;
    private Vector2d shipPos;
    private Vector2d propellorPos;

    private Image ship;
    private Image shipProp;
    private Image cargo;
    
    private Image logo;

	public Platform(AirshipGame game, String name) {
		super(game, name);

        this.image = Assets.getImage(name);
        ship 	   = Assets.getImage("ship/Interceptor-normal");
        shipProp   = Assets.getImage("ship/Interceptor-prop1");
        cargo 	   = Assets.getImage("Menu/cargo");
        logo 	   = Assets.getImage("Menu/logo");
        
        Graphics g = game.getGraphics();
        
        setPos(new Vector2d(0, g.getHeight() - this.image.getHeight()));
        
        cargoPos     = new Vector2d(60, 67);
        shipPos      = new Vector2d(28, 32);
        propellorPos = new Vector2d(shipPos.x + ship.getWidth()/2 - shipProp.getWidth()/2,
                shipPos.y + ship.getHeight());

	}

    public int getAnimationDuration() {
        return ANIMATION_DURATION;
    }

	public Image getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = Assets.getImage(image);
	}

	@Override
	public void onUpdate(double deltaSeconds) {
		
	}

	@Override
	public void onPaint(float deltaSeconds) {
		
		Graphics g = game.getGraphics();
		g.drawImage(getImage(), getPos());
		g.drawImage(cargo, getPos().add(cargoPos));
		g.drawImage(ship, getPos().add(shipPos));
		g.drawImage(shipProp, getPos().add(propellorPos));
		g.drawImage(logo, new Vector2d(g.getWidth()/2 - logo.getWidth() / 2,
				g.getHeight() - getPos().y - logo.getHeight() - logo.getHeight() / 5));
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		
	}
}
