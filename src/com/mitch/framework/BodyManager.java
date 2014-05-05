package com.mitch.framework;

import java.util.ArrayList;
import java.util.List;

import com.mitch.flyship.GameBody;

public class BodyManager {
	
	private final List<GameBody> bodies;
	
	public BodyManager()
	{
		bodies = new ArrayList<GameBody>();
	}
	
	public List<GameBody> getBodies()
	{
		return bodies;
	}
	
	public void addBody(GameBody body)
	{
		bodies.add(body);
	}
	
	public void removeBody(GameBody body)
	{
		bodies.remove(body);
	}
	
	public GameBody findFirstBody(String name)
	{
		for (GameBody body : bodies) {
			if (body.getName() == name) {
				return body;
			}
		}
		return null;
	}
	
	public List<GameBody> findAllBodies(String name)
	{
		List<GameBody> foundBodies = new ArrayList<GameBody>();
		for (GameBody body : bodies) {
			if (body.getName() == name) {
				foundBodies.add(body);
			}
		}
		return foundBodies;
	}
	
	
}
