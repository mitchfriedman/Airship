package com.mitch.framework.containers;


public class Rect {
	public double x, y;
	public double width, height;
	
	public Rect(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rect(Vector2d position, Vector2d size)
	{
		this.x = position.x;
		this.y = position.y;
		this.width = size.x;
		this.height = size.y;
	}
	
	public Rect(Rect rect)
	{
		this.x = rect.x;
		this.y = rect.y;
		this.width  = rect.width;
		this.height = rect.height;
	}
	
	public Vector2d getRealPosition()
	{
		return new Vector2d(x,y);
	}
	
	public Vector2i getIntPosition()
	{
		return new Vector2i(x,y);
	}
	
	public void setPosition(Vector2d position)
	{
		this.x = position.x;
		this.y = position.y;
	}
	
	
	public Vector2d getRealSize()
	{
		return new Vector2d(width,height);
	}
	
	public Vector2i getIntSize()
	{
		return new Vector2i(width,height);
	}
	
	public void setSize(Vector2d size)
	{
		this.width = size.x;
		this.height = size.y;
	}
	
	
	public static boolean rectCollides(Rect a, Rect b)
	{
		double startX = a.x < b.x ? a.x : b.x;
		double startY = a.y < b.y ? a.y : b.y;
		double endX = a.y+a.width  > b.x+b.width  ? a.x+a.width  : b.x+b.width;
		double endY = a.y+a.height > b.y+b.height ? a.y+a.height : b.y+b.height;
		
		boolean withinX = a.width  + b.width  <= endX-startX;
		boolean withinY = a.height + b.height <= endY-startY;
		
		return withinX && withinY;
	}
	
	public static boolean vectorWithinRect(Vector2d vec, Rect rect)
	{
		
		boolean withinX = vec.x <= rect.x + rect.width  && vec.x >= rect.x;
		boolean withinY = vec.y <= rect.y + rect.height && vec.y >= rect.y;
		
		return withinX && withinY;
	}
}
