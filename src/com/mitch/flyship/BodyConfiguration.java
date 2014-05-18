package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import com.mitch.framework.containers.Vector2d;

public class BodyConfiguration {
	
	public final List<Vector2d> points;
	public final List<String> types;
	public final Vector2d maxObjectSize;
	public final Vector2d size;
	
	public BodyConfiguration(Vector2d maxObjectSize)
	{
		this.points = new ArrayList<Vector2d>();
		this.types = new ArrayList<String>();
		this.maxObjectSize = maxObjectSize;
		this.size = Vector2d.ZERO;
	}
	
	public void addConfigurationObject(Vector2d point, String type)
	{
		points.add(point);
		types.add(type);
		
		size.x = point.x > size.x ? point.x : size.x;
		size.y = point.y > size.y ? point.y : size.y;
	}
	
	public Vector2d getConfigurationSize()
	{
		return size.add(maxObjectSize);
	}
}
