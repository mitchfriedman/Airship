package com.mitch.framework;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.mitch.flyship.screens.Level;

public class LevelEventManager {
	final Level level;
	
	List<Event> events = new ArrayList<Event>();
	int currentEventIndex = 0;
	float lastEventTime = 0;
	
	enum Events {
		SPEED_CHANGE
	}
	
	public LevelEventManager(Level level)
	{
		this.level = level;
	}
	
	public void loadEvents()
	{
		/*List<String> attributes = new ArrayList<String>();
		attributes.add("20");
		Event event = new Event("SPEED_CHANGE", 5, attributes);
		events.add(event);*/
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
		case SPEED_CHANGE:
			double speed = Double.parseDouble(event.attributes.get(0));
			level.setSpeed(speed);
			break;
		}
	}
}
