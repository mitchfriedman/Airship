package com.mitch.flyship.Enemy.Components;

import java.util.ArrayList;
import java.util.List;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Coin;
import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.objects.Ship;
import com.mitch.flyship.objects.Water;
import com.mitch.framework.Image;
import com.mitch.framework.containers.MathHelper;

public class ItemContainer extends EnemyComponent {

	//private List<Image> images;
	private Enemy mine;
	private Coin coin;
	private Water water;
	
	private InsideCrate inside;
	private List<Float> weights = new ArrayList<Float>();
	
	private enum InsideCrate {
		WATER,
		COIN,
		MINE
	}
	
	public ItemContainer() {
		weights.add(25f);
		weights.add(50f);
		weights.add(25f);
	}
	
	public ItemContainer(final XmlResourceParser parser) {
		this();	
	}
	
	@Override
	public void onComponentAdded() {
		super.onComponentAdded();
		
		inside = getItemInsideCrate(MathHelper.generateRandomValueFromWeights(weights));
	}
	
	@Override
	public void onHit(Ship ship) {
		super.onHit(ship);
		
		if(inside == InsideCrate.COIN) {
			Log.d("Breakable", "GENERATING COIN");
			generateCoin();
		} else if(inside == InsideCrate.WATER) {
			Log.d("Breakable", "GENERATING WATER");
			generateWater();
		} else {
			Log.d("Breakable", "GENERATING MINE");
			generateMine();
		}
	}

	@Override
	public EnemyComponent clone() {
		ItemContainer enemy = new ItemContainer();
		enemy.weights = weights;
		return enemy;
	}
	
	
	private void generateMine() {
		mine = new Enemy(enemy.getLevel(), "MINE");
		mine.setDamage(1);
		mine.setDepth(enemy.getDepth());
		mine.addComponent(new StaticImage("Enemy/mine", false, false));
		mine.addComponent(new ExplosionAnimation());
		mine.setPos(enemy.getPos().add(enemy.getSize().divide(2)));
		enemy.getLevel().getBodyManager().addBodyDuringUpdate(mine);
	}
	
	private void generateWater() {
		water = new Water(enemy.getLevel());
		water.setPos(enemy.getPos().add(enemy.getSize().divide(2)));
		enemy.getLevel().getBodyManager().addBodyDuringUpdate(water);
	}
	
	private void generateCoin() {
		coin = new Coin(enemy.getLevel(), Coin.CoinType.TEN);
		coin.setPos(enemy.getPos().add(enemy.getSize().divide(2)));
		enemy.getLevel().getBodyManager().addBodyDuringUpdate(coin);
	}
	
	private InsideCrate getItemInsideCrate(int index) {
		switch(index) {
			case 1: 
				return InsideCrate.WATER;
			case 2: 
				return InsideCrate.COIN;
			case 3: 
				return InsideCrate.MINE;
			default: 
				return InsideCrate.MINE;
		}
	}
}
