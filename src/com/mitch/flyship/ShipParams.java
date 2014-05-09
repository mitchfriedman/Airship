package com.mitch.flyship;

import com.mitch.framework.Image;

public class ShipParams {
	
	public Image left;
	public Image normal;
	public Image right;
	
	public ShipParams(String name)
	{
		this.left = Assets.getImage("ship/" + name + "/left");
		this.normal = Assets.getImage("ship/" + name + "/normal");
		this.right = Assets.getImage("ship/" + name + "/right");
	}
}