package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;

import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;


public class Platform extends GameBody {
	
	Image image;

    private Vector2d cargoPos;
    private Vector2d shipPos;
    private Vector2d propellorPos;
    private Vector2d platformPos;

    private Image ship;

	public Platform(AirshipGame game, String name) {
		super(game, name);

        Graphics g = game.getGraphics();

        this.image = Assets.getImage(name);

        ship = Assets.getImage("ship/Interceptor-normal");

        platformPos = new Vector2d(0, game.getGraphics().getHeight()-image.getHeight());
        cargoPos = new Vector2d(g.getWidth()/2+30, platformPos.y+60);
        shipPos = new Vector2d(g.getWidth()/2-10, platformPos.y+25);
        propellorPos = new Vector2d(shipPos.x + ship.getWidth()/2 - Assets.getImage("ship/Interceptor-prop1").getWidth()/2,
                shipPos.y + ship.getHeight());

	}

	public Image getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = Assets.getImage(image);
	}

	@Override
	public void onUpdate(double deltaTime) {
		
	}

	@Override
	public void onPaint(float deltaTime) {

		Graphics g = game.getGraphics();
		g.drawImage(getImage(), platformPos.x, platformPos.y);
		g.drawImage(Assets.getImage("Menu/cargo"), cargoPos.x, cargoPos.y);
        g.drawImage(Assets.getImage("Menu/bottom border"), 0, g.getHeight()-3);
		g.drawImage(ship, shipPos.x, shipPos.y);
		g.drawImage(Assets.getImage("ship/Interceptor-prop1"), propellorPos.x, propellorPos.y);
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {		
	}
}
