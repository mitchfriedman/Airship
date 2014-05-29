package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import com.mitch.framework.Image;
import com.mitch.framework.containers.Rect;
import com.mitch.framework.containers.Vector2d;

public class ShipParams {
	
	final public Image tilt0;
	final public Image tilt1;
	final public Image tilt2;
	final public Image propellerAnimImage;
	
	final public double animationTime;
	final public Vector2d propellerPos;
	final public List<Rect> animationRects;
	
	final public int coinCollectionRange;
	final public int coinAttractionRange;
	final public int coinAttractionSpeed;
	
	final public Rect collisionOffset;
	
	public ShipParams(String type)
	{
		this.tilt0 = Assets.getImage("ship/" + type + "-normal");
		this.tilt1 = Assets.getImage("ship/" + type + "-tilt1");
		this.tilt2 = Assets.getImage("ship/" + type + "-tilt2");
		
		this.propellerAnimImage = Assets.getImage("ship/" + type + "-prop_anim");
		this.propellerPos = new Vector2d(9, 61);
		this.animationTime = 0.1;
		
		this.animationRects = new ArrayList<Rect>();
		this.animationRects.add(new Rect(0,0,  13,4));
		this.animationRects.add(new Rect(2,0,  13,4));
		this.animationRects.add(new Rect(0,0,  13,4));
		this.animationRects.add(new Rect(0,0,  13,4));
		
		this.coinCollectionRange = 3;
		this.coinAttractionRange = 30;
		this.coinAttractionSpeed = 3;
		
		// 8 from left, ten from top, 8 from right, 3 from bottom
		this.collisionOffset = new Rect(8,10,8,3);
	}
}