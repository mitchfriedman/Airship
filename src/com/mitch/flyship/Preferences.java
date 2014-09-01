package com.mitch.flyship;

import android.content.SharedPreferences;
import android.util.Log;

public class Preferences {
	
	public static float DEFAULT_SENSITIVITY = 1;
	
	enum KEYS {
		SENSITIVITY
	}
	
	static SharedPreferences preferences;
	static SharedPreferences.Editor editor;
	
	Preferences() {};
	
	public static SharedPreferences getPreferences()
	{
		return preferences;
	}
	
	public static SharedPreferences.Editor getPreferencesEditor()
	{
		return editor;
	}
	
	public static void loadPreferences(SharedPreferences preferences)
	{
		Preferences.preferences = preferences;
		Preferences.editor = preferences.edit();
	}
	
	public static void resetDefaults()
	{
		editor.putFloat(KEYS.SENSITIVITY.toString(), DEFAULT_SENSITIVITY);
		editor.apply();
	}
	
	public static void putSensitivity(float percent)
	{
		editor.putFloat(KEYS.SENSITIVITY.toString(), percent);
		editor.apply();
	}
	
	public static float retrieveSensitivity()
	{
		Log.d("RETRIEVING SENSITIVITY", "" + preferences.getFloat(KEYS.SENSITIVITY.toString(), DEFAULT_SENSITIVITY));
		return preferences.getFloat(KEYS.SENSITIVITY.toString(), DEFAULT_SENSITIVITY);
	}
}
