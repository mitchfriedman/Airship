package com.mitch.flyship.Enemy.Components;

import java.util.ArrayList;
import java.util.List;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.objects.Coin;
import com.mitch.flyship.objects.Enemy;
import com.mitch.flyship.objects.Ship;
import com.mitch.flyship.objects.Water;
import com.mitch.framework.containers.MathHelper;
import com.mitch.framework.containers.Vector2d;

public class ItemContainer extends EnemyComponent {

	//private List<Image> images;
	private Enemy mine;
	private Coin coin;
	private Water water;
	private boolean generated = false;
	
	private InsideCrate inside;
	private List<Float> weights = new ArrayList<Float>();
	
	private enum InsideCrate {
		WATER,
		COIN,
		MINE,
		NOTHING
	}
	
	public ItemContainer() {
		weights.add(10f); // water
		weights.add(35f); // coin
		weights.add(45f); //mine
		weights.add(10f); // nothing
	}
	
	public ItemContainer(final XmlResourceParser parser) {
		this();	
	}
	
	@Override
	public void onComponentAdded() {
		super.onComponentAdded();
		
		inside = getItemInside(MathHelper.generateRandomValueFromWeights(weights));
	}
	
	@Override
	public void onUpdate(double deltaTime) {
		super.onUpdate(deltaTime);
		
		if (enemy.isTouched(new Vector2d(0,0))) {
			onTouched();
		}
	}
	
	public void generateItem()
	{
		if (!generated) {
			switch(inside) {
			case COIN:
				generateCoin();
				break;
			case WATER:
				generateWater();
				break;
			case MINE:
				generateMine();
				break;
			case NOTHING:
			default:
				break;
			}
			
			generated = true;
		}
	}
	
	public void onTouched()
	{
		generateItem();
	}
	
	@Override
	public void onHit(Ship ship) {
		super.onHit(ship);
		generateItem();
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
		mine.setDepth(enemy.getDepth()-1);
		mine.addComponent(new StaticImage("Enemy/mine", false, false));
		mine.addComponent(new ExplosionAnimation());
		mine.setPos(enemy.getPos().add(enemy.getSize().divide(2)));
		mine.setPos(mine.getPos().subtract(mine.getSize().divide(2)));
		enemy.getLevel().getBodyManager().addBodyDuringUpdate(mine);
	}
	
	private void generateWater() {
		water = new Water(enemy.getLevel());
		water.setPos(enemy.getPos().add(enemy.getSize().divide(2)));
		water.setPos(water.getPos().subtract(water.getSize().divide(2)));
		water.setDepth(enemy.getDepth()-1);
		enemy.getLevel().getBodyManager().addBodyDuringUpdate(water);
	}
	
	private void generateCoin() {
		coin = new Coin(enemy.getLevel(), Coin.CoinType.TEN);
		coin.setPos(enemy.getPos().add(enemy.getSize().divide(2)));
		//coin.setPos(coin.getPos().subtract(coin.getSize().divide(2)));
		coin.setDepth(enemy.getDepth()-1);
		enemy.getLevel().getBodyManager().addBodyDuringUpdate(coin);
	}
	
	private InsideCrate getItemInside(int index) {
		return InsideCrate.values()[index];
	}
}
