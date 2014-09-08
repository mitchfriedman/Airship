package com.mitch.flyship;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.mitch.flyship.screens.Level;
import com.mitch.flyship.screens.Loading;
import com.mitch.framework.Screen;
import com.mitch.framework.containers.Vector2d;
import com.mitch.framework.implementation.AndroidGame;

public class AirshipGame extends AndroidGame {
	
	public static final float MAX_SENSITIVITY = 2.0f;
	public static final float MIN_SENSITIVITY = 0.3f;
	public static final float DEFAULT_SENSITIVITY = 1;
    public static final boolean SHOW_COLLIDER_BOXES = false;
    public static final boolean DEBUG = true;
    public static boolean muted = false;
	
    private AchievementManager achieveManager;
    private Vector2d centerOrientation = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Preferences.loadPreferences(PreferenceManager.getDefaultSharedPreferences(this));
		
        Preferences.DEFAULT_SENSITIVITY = DEFAULT_SENSITIVITY / (MAX_SENSITIVITY - MIN_SENSITIVITY);
        
        achieveManager = new AchievementManager(this);

	}
	
	public AchievementManager getAchievementManager()
	{
		return achieveManager;
	}
	
    @Override
	public Screen getInitScreen() 
	{
		if (!Assets.isLoaded()) {
			new LoadAssetsTask().execute(this);
		}
		
		return new Loading(this);
	}
	
	@Override
	public void onBackPressed() 
	{
		getCurrentScreen().backButton();
	}
	
	public double getSensitivity()
	{
		return Preferences.retrieveSensitivity();
	}
	
	public void setSensitivity(double sensitivity)
	{
		Preferences.putSensitivity((float) sensitivity);
	}
	
	public void centerOrientation()
	{
		centerOrientation = getInput().getTilt();
		if (getCurrentScreen().getClass().equals(Level.class)) {
			Level level = (Level) getCurrentScreen();
			level.getBodyManager().getShip().getPlayer().centerOrientation();
		}
		
	}
	
	public Vector2d getCenterOrientation()
	{
		return centerOrientation;
	}
	
	public ShipParams loadMerchantShipParams()
	{
		ShipParams params = new ShipParams("Interceptor");
		return params;
	}

    public void pushScore(String leaderboard, long score)
    {
        apiManager.pushLeaderboardScore(leaderboard, score);
    }
    
    public void loadBoard(String leaderboard)
    {
    	apiManager.loadBoard(leaderboard);
    }
    
    public void loadBoards()
    {
    	apiManager.loadBoards();
    }

}
