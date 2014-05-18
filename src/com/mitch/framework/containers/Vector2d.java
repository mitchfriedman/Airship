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
	
	public Vector2d add(Vector2d vec)
	{
		return new Vector2d(x+vec.x, y+vec.y);
	}
	
	public Vector2d subtract(Vector2d vec)
	{
		return new Vector2d(x-vec.x, y-vec.y);
	}
	
	public Vector2d scale(double scale)
	{
		return new Vector2d(x*scale, y*scale);
	}
	
	public Vector2d divide(double numerator)
	{
		return new Vector2d(x/numerator, y/numerator);
	}
	
	public Vector2d scaleY(double scaledX)
	{
		double scaledY = scaledX * (y/x);
		return new Vector2d(scaledX, scaledY);
	}
	
	public Vector2d scaleX(double scaledY)
	{
		double scaledX = scaledY * (x/y);
		return new Vector2d(scaledX, scaledY);
	}
	
	public double getLength()
	{
		return Math.sqrt(x*x+y*y);
	}
	
	public double getDistance(Vector2d b)
	{
		Vector2d displacement = this.subtract(b);
		return Math.abs(displacement.getLength());
	}
	
	public Vector2d normalize()
	{
		return this.divide(this.getLength());
	}
}
