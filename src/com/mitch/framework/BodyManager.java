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
	
	
}
