package com.mitch.flyship;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.Log;

@SuppressWarnings(value = { "unused" })
public class LevelProperties {

	private String background, music, ship;
	private int maxLevel, timeToMaxLevel;
	private double startSpeed, endSpeed;
	private int waterSpawnTimeStart, waterSpawnTimeEnd;
	private int waterValue, waterValueDrainTime;


	public LevelProperties()
	{
		maxLevel = 100;
		timeToMaxLevel = 900000;
	}

	public LevelProperties(XmlResourceParser xrp)
	{
		loadFromXML(xrp);
	}

	public void loadBackgroundImage(String image) 
	{
		this.background = image;
	}

	public void loadFromXML(XmlResourceParser xrp) 
	{
		while(true) 
		{
			try 
			{
				int eventType = xrp.next();
				if (eventType == XmlResourceParser.END_DOCUMENT) {
					break;
				}
				if (eventType != XmlResourceParser.START_TAG) {
					continue;
				}

			} catch (XmlPullParserException e) {
				e.printStackTrace();
				Log.d("XmlPullParserException", e.getLocalizedMessage());
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				Log.d("IOException", e.getLocalizedMessage());
				continue;
			}

			String type   = xrp.getName();
			String value  = xrp.getAttributeValue(null, "value");


			if (value == null || type == null) {
				Log.d("XML Asset Error", "Key or path is null.");
			}
			else if (type.equals("background")) {
				loadBackgroundImage(value);
			}
			else if (type.equals("speed")) {
				setSpeed(value);		
			}
			else {
				Log.d("XML Asset Error", "No tag with name " + type);
			}
		}

	}

	public void setSpeed(String speed) 
	{
		//this.speed = Double.parseDouble(speed);
	}
}
