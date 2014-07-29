package com.mitch.flyship.objects;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.Assets;
import com.mitch.flyship.GameBody;

import com.mitch.framework.Graphics;
import com.mitch.framework.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mitch.framework.containers.Vector2d;


public class Platform extends GameBody {
	
	Image image;

    private static final int ANIMATION_DURATION = 10000;

    private Vector2d cargoPos;
    private Vector2d shipPos;
    private Vector2d propellorPos;
    private Vector2d platformPos;

    private Image ship;

    private float elapsedTime = 0;

    //private List<Vector2d> gameBodyPositions;
   // private List<Vector2d> removeQueue;

    private boolean startingGame;
    private boolean readyToStart;

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

        //setPos(platformPos);

        //gameBodyPositions = new ArrayList<Vector2d>();
        //removeQueue = new ArrayList<Vector2d>();
        //gameBodyPositions.addAll(Arrays.asList(cargoPos, getPos()));

        startingGame = false;
        readyToStart = false;

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
	public void onUpdate(double deltaTime) {
		
	}

    private void clearBodies(Vector2d endPosPlatform, Vector2d endPosCargo,
                             Vector2d startPosPlatform, Vector2d startPosCargo, float elapsedTime) {

        Vector2d posDifferencePlatform = endPosPlatform.subtract(startPosPlatform);
        Vector2d posDifferenceCargo = endPosCargo.subtract(startPosCargo);

        float tweenPosRatio = elapsedTime / ANIMATION_DURATION;

        platformPos = new Vector2d(startPosPlatform.x + posDifferencePlatform.x * tweenPosRatio,
                startPosPlatform.y + posDifferencePlatform.y * tweenPosRatio);

        cargoPos = new Vector2d(startPosCargo.x + posDifferenceCargo.x * tweenPosRatio,
                startPosCargo.y + posDifferenceCargo.y * tweenPosRatio);

/*
        positionDifference = ENDPOS-STARTPOS
        tweenPositionRatio = elapsedTime/INTERVAL;
        targetPosition = STARTPOS + positionDifference * tweenPositionRatio



        /*if(gameBodyPositions.size() == 0) {
            readyToStart = true;
        } else {
            readyToStart = false;

            for(Vector2d pos : gameBodyPositions) {
                pos.y += moveSpeed;
                if(pos.y >= game.getGraphics().getHeight()) {
                    //gameBodyPositions.remove(pos);
                    removeQueue.add(pos);
                }

            }
        }
        for(Vector2d pos : removeQueue) {
            gameBodyPositions.remove(pos);
        }
        removeQueue.clear();*/
        //cargoPos.y += moveSpeed;
        //platformPos.y += moveSpeed;

    }

    public void startGame() {
        startingGame = true;
    }

    public boolean getStartingGame() {
        return startingGame;
    }

    public boolean readyToStart() {
        return readyToStart;
    }

	@Override
	public void onPaint(float deltaTime) {

        if(startingGame) {

            Vector2d startPosPlatform = platformPos;
            Vector2d startPosCargo = cargoPos;

            Vector2d endPosPlatform = new Vector2d(0, game.getGraphics().getHeight());
            Vector2d endPosCargo = new Vector2d(cargoPos.x, cargoPos.y + game.getGraphics().getHeight());

            elapsedTime += deltaTime;

            clearBodies(endPosPlatform, endPosCargo, startPosPlatform, startPosCargo, elapsedTime);
        }

        if(elapsedTime >= ANIMATION_DURATION) {
            readyToStart = true;
        }

		Graphics g = game.getGraphics();
		g.drawImage(getImage(), platformPos.x, platformPos.y);
		g.drawImage(Assets.getImage("Menu/cargo"), cargoPos.x, cargoPos.y);
        if(!readyToStart) {
            g.drawImage(Assets.getImage("Menu/bottom border"), 0, g.getHeight()-3);
        }
		g.drawImage(ship, shipPos.x, shipPos.y);
		g.drawImage(Assets.getImage("ship/Interceptor-prop1"), propellorPos.x, propellorPos.y);
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
}
