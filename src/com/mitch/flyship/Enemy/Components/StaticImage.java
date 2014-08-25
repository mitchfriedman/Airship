package com.mitch.flyship.Enemy.Components;

import android.content.res.XmlResourceParser;

import com.mitch.flyship.Assets;
import com.mitch.flyship.Enemy.EnemyComponent;
import com.mitch.framework.Graphics;
import com.mitch.framework.Image;

/**
 * Created by KleptoKat on 7/20/2014.
 */
public class StaticImage extends EnemyComponent {

    Image image;
    boolean invertHorizontal;
    boolean invertVertical;
    boolean drawing = true;

    public StaticImage() {}
    
    public StaticImage(String imageID, boolean invertHorizontal, boolean invertVertical)
    {
    	this.image = Assets.getImage(imageID);
    	this.invertHorizontal = invertHorizontal;
    	this.invertVertical = invertVertical;
    }
    
    public StaticImage(XmlResourceParser parser) //xml stuff here
    {
        this();
        String imageID = parser.getAttributeValue(null, "image");
        image = Assets.getImage(imageID);
        invertHorizontal = parser.getAttributeBooleanValue(null, "invertHorizontal", false);
        invertVertical = parser.getAttributeBooleanValue(null, "invertVertical", false);
    }

    public void setDrawing(boolean drawing)
    {
        this.drawing = drawing;
    }

    @Override
    public void onComponentAdded() {
        super.onComponentAdded();
        enemy.setSize(image.getSize());
    }

    @Override
    public void onPaint(float deltaTime) {
        super.onPaint(deltaTime);
        if (drawing) {
            Graphics g = enemy.getLevel().getAirshipGame().getGraphics();
            g.drawImage(image, enemy.getPos(), invertHorizontal, invertVertical);
        }

    }

    @Override
    public EnemyComponent clone() {
        StaticImage component = new StaticImage();
        component.invertHorizontal = this.invertHorizontal;
        component.invertVertical = this.invertVertical;
        component.image = this.image;
        return component;
    }
}
