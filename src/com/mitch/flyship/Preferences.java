package com.mitch.flyship;

import android.content.SharedPreferences;

public class Preferences {
	
	public static float DEFAULT_SENSITIVITY_PERCENT = 1;
	
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
	
	public static void loadPreferences(SharedPreferences preferences, SharedPreferences.Editor preferencesEditor)
	{
		Preferences.preferences = preferences;
		Preferences.editor = preferencesEditor;
	}
	
	public static void resetDefaults()
	{
		editor.putFloat(KEYS.SENSITIVITY.toString(), DEFAULT_SENSITIVITY_PERCENT);
		editor.commit();
	}
	
	public static void putSensitivityInPercent(float percent)
	{
		editor.putFloat(KEYS.SENSITIVITY.toString(), percent);
		editor.commit();
	}
	
	public static float retrieveSensitivityInPercent()
	{
		return preferences.getFloat(KEYS.SENSITIVITY.toString(), DEFAULT_SENSITIVITY_PERCENT);
	}
}
