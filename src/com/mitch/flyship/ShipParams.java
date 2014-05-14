package com.mitch.flyship;

import com.mitch.framework.Image;

public class ShipParams {
	
	public Image left;
	public Image normal;
	public Image right;
	
	public ShipParams(String type)
	{
		this.left = Assets.getImage("ship/" + type + "-left");
		this.normal = Assets.getImage("ship/" + type + "-normal");
		this.right = Assets.getImage("ship/" + type + "-right");
	}
}