package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import com.mitch.framework.Image;
import com.mitch.framework.containers.Frame;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class ShipParams {
	
	final public Image tilt0;
	final public Image tilt1;
	final public Image tilt2;
	
	final public float animation_fps;
	final public Vector2d propellerPos;
	final public List<Frame> animationRects;
	
	final public int coinCollectionRange;
	final public int coinAttractionRange;
	final public int coinAttractionSpeed;
	
	final public int waterCollectionRange;
	final public int waterAttractionRange;
	final public double waterAttractionSpeed;
	
	final public Rect collisionOffset;
	
	public ShipParams(String type)
	{
		this.tilt0 = Assets.getImage("ship/" + type + "-normal");
		this.tilt1 = Assets.getImage("ship/" + type + "-tilt1");
		this.tilt2 = Assets.getImage("ship/" + type + "-tilt2");
		
		this.animation_fps = 16;
		this.propellerPos = new Vector2d(9, 61);
		
		this.animationRects = new ArrayList<Frame>();
		this.animationRects.add(new Frame(Assets.getImage("ship/Interceptor-prop1"), false, false ));
		this.animationRects.add(new Frame(Assets.getImage("ship/Interceptor-prop2"), false, false ));
		this.animationRects.add(new Frame(Assets.getImage("ship/Interceptor-prop1"), true,  false ));
		this.animationRects.add(new Frame(Assets.getImage("ship/Interceptor-prop2"), true,  false ));
		
		this.coinCollectionRange = 3;
		this.coinAttractionRange = 30;
		this.coinAttractionSpeed = 6;
		
		this.waterCollectionRange = 3;
		this.waterAttractionRange = 15;
		this.waterAttractionSpeed = 4;
		
		
		// 8 from left, ten from top, 8 from right, 3 from bottom
		this.collisionOffset = new Rect(8,10,-8,-3);
	}
}