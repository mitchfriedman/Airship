package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.Assets;
import com.mitch.flyship.BodyConfiguration;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;
import com.mitch.framework.containers.Vector2d;

public class Coin extends GameBody {
	
	final Image image;
	public final int value;
	
	Level level;
	
	public enum CoinType {
		GOLD,
		SILVER
	}
	
	public Coin(Level level, CoinType type)
	{
		super(level.getAirshipGame(), type == CoinType.GOLD ? "gold_coin" : "silver_coin");
		this.level = level;
		this.image = type == CoinType.GOLD ? Assets.getImage("gold_coin") : Assets.getImage("silver_coin");
		this.value = type == CoinType.GOLD ? 10 : 1;
		setSize(image.getSize());
		offset = image.getSize().divide(2);
	}
	
	@Override
	public void onUpdate(float deltaTime) {
		if (getPos().y > game.getGraphics().getHeight()) {
			level.getBodyManager().removeBody(this);
		}
	}

	@Override
	public void onPaint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(image, getPos().subtract(offset));
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}
	
	public static List<Coin> getBodiesFromConfiguration(BodyConfiguration config,
			Vector2d pos, Level level) 
	{
		List<Coin> coins = new ArrayList<Coin>();
		for (int i = 0; i < config.points.size(); i++) {
			CoinType coinType = config.types.get(i) == "silver" ? CoinType.SILVER : CoinType.GOLD;
			Coin coin = new Coin(level, coinType);
			coin.setPos(pos.add(config.points.get(i)));
			coins.add(coin);
		}
		return coins;
	}
}
