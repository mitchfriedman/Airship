package com.mitch.flyship;

import java.util.ArrayList;
import java.util.List;

import com.mitch.framework.containers.Vector2d;

public class BodyConfiguration {
	
	public final List<Vector2d> points;
	public final List<String> types;
	public Vector2d size;
	
	public BodyConfiguration()
	{
		this(Vector2d.ZERO);
	}
	
	public BodyConfiguration(Vector2d size)
	{
		this.points = new ArrayList<Vector2d>();
		this.types = new ArrayList<String>();
		setSize(size);
	}
	
	public void addConfigurationObject(Vector2d point, String type)
	{
		points.add(point);
		types.add(type);
	}
	
	public void setSize(Vector2d size)
	{
		this.size = size;
	}
	
	public Vector2d getConfigurationSize()
	{
		return size;
	}
}
