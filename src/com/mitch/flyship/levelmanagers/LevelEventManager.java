package com.mitch.flyship.levelmanagers;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.mitch.flyship.screens.Level;
import com.mitch.framework.Event;
import com.mitch.framework.implementation.AndroidGame;

public class LevelEventManager {
	final Level level;
	
	List<Event> events = new ArrayList<Event>();
	int currentEventIndex = 0;
	float lastEventTime = 0;
	
	enum Events {
		SPEED_CHANGE,
		CHANGE_SMOOTHING
	}
	
	public LevelEventManager(Level level)
	{
		this.level = level;
	}
	
	public void loadEvents()
	{
		/*List<String> attributes = new ArrayList<String>();
		
		Event event;
		
		for (int i = 1; i <= 5; i++) {
			attributes.clear();
			attributes.add(String.valueOf(i));
			event = new Event("CHANGE_SMOOTHING", 5, attributes);
			events.add(event);
		}*/
		
		
	}
	
	public void update()
	{
		// If all events fired return
		if (currentEventIndex >= events.size())
			return;
		
		Event currentEvent = events.get(currentEventIndex);
		float elapsedTime = level.getElapsedTime_s();
		
		if (currentEvent.triggerTime_s <= elapsedTime) {
			fireEvent(currentEvent);
			currentEventIndex++;
			lastEventTime = elapsedTime;
		}
		
	}
	
	public void fireEvent(Event event)
	{
		Log.d("FIRING EVENT", event.name);
		Events eventType = Events.valueOf(event.name);
		
		switch (eventType) {
		case CHANGE_SMOOTHING:
			AndroidGame.SCALE = Float.parseFloat(event.attributes.get(0));
			level.getAirshipGame().createFrameBuffer();
			break;
		}
	}
}
