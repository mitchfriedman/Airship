package com.mitch.flyship.objects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.util.Log;

import com.mitch.flyship.AirshipGame;
import com.mitch.flyship.GameBody;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.flyship.Enemy.EnemyProperties;
import com.mitch.flyship.Enemy.Components.VerticalEnemy;
import com.mitch.flyship.screens.Level;
import com.mitch.framework.containers.Vector2d;

public class Enemy extends GameBody {

    private boolean colliding = true;
    private boolean destroyingOnHit = true;
    private List<EnemyComponent> components;
    private int damage = 0;
	private Level level;
	
	
    public static Enemy loadFromProperties(Level level, EnemyProperties properties)
    {
        Enemy enemy = new Enemy(level, properties.getName());
        enemy.setDamage(properties.getDamage());
        for (EnemyComponent component : properties.getComponents()) {
            if (!enemy.addComponent(component.clone())) {
                Log.d("Enemy: " + enemy.getName(), "Component " +
                        component.getClass().getName() +
                        " is already on this enemy.");
            }
        }

        for (EnemyComponent component : enemy.getComponents()) {
            component.onObjectCreationCompletion();
        }

        return enemy;
    }

    public Level getLevel() { return level; }
    public int getDamage()
    {
        return damage;
    }
    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }
    public boolean isColliding() {
        return colliding;
    }
    public void setDamage(int damage) { this.damage = damage; }
    public void setDestroyingOnHit(boolean destroyingOnHit) { this.destroyingOnHit = destroyingOnHit; }

	public Enemy(Level level, String name)
	{
		super(level.getAirshipGame(), name);
        components = new ArrayList<EnemyComponent>();
		this.level = level;
	}
	
	@Override
	public void onUpdate(double deltaTime)
	{
        Vector2d velocity = getVelocity().scale( deltaTime );
        setPos(getPos().add( velocity ));

		for (EnemyComponent component : components) {
            component.onUpdate(deltaTime);
        }
	}
	
	@Override
	public void onPaint(float deltaTime)
    {
        for (EnemyComponent component : components) {
            component.onPaint(deltaTime);
        }

        if (AirshipGame.SHOW_COLLIDER_BOXES) {
            game.getGraphics().drawRect(this.getCollisionBounds(), Color.RED);
        }
    }

    public void onHit(Ship ship)
    {
        for (EnemyComponent component : components) {
            component.onHit(ship);
        }

        if (destroyingOnHit) {
            level.getBodyManager().removeBody(this);
        }
    }

    public boolean addComponent(EnemyComponent component)
    {
        if (!componentExists(component.getClass())) {
            component.setEnemy(this);
            components.add(component);
            component.onComponentAdded();
            return true;
        }
        return false;
    }
    
    public void removeComponent(Class<? extends EnemyComponent> clazz) {
    	if(componentExists(clazz)) {
			for(EnemyComponent component : components) {
				if(component.getClass().equals(clazz.getClass())) {
					components.remove(component);
				}
			}
    	}
    }

    public List<EnemyComponent> getComponents()
    {
        return components;
    }

    @SuppressWarnings("unchecked")
	public<T extends EnemyComponent> T getComponent(Class<T> cls)
    {
        for (EnemyComponent component : components) {
            if (component.getClass() == cls) {
                return (T)component;
            }
        }
        return null;
    }

    public boolean componentExists(Class<? extends EnemyComponent> componentType)
    {
        return getComponent(componentType) != null;
    }

	public static List<GameBody> spawnObjects(Level level, String type)
	{
        for (EnemyProperties template : level.getEnemyTypes()) {
            if (template.getName() == type) {
                // Enemy configurations (multiple enemies spawning in formation)
                // are possible however you would probably need to
                // hard code them in here. For a more elegant solution refactoring is required.

                List<GameBody> enemies = new ArrayList<GameBody>();
                enemies.add(loadFromProperties(level, template));
                return enemies;

            }
        }

        return null;
	}

}
