package com.mitch.framework.containers;

public class Vector2d {
	
	public static final Vector2d ZERO = new Vector2d(0,0);
	
	public double x, y;
	
	public Vector2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
}
