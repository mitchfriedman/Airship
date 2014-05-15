package com.mitch.flyship;

import com.mitch.framework.Image;

public class ShipParams {
	
	final public Image tilt0;
	final public Image tilt1;
	final public Image tilt2;
	
	public ShipParams(String type)
	{
		this.tilt0 = Assets.getImage("ship/" + type + "-normal");
		this.tilt1 = Assets.getImage("ship/" + type + "-tilt1");
		this.tilt2 = Assets.getImage("ship/" + type + "-tilt2");
	}
}