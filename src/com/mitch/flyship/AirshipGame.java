package com.mitch.flyship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mitch.flyship.screens.Loading;
import com.mitch.framework.Screen;
import com.mitch.framework.implementation.AndroidGame;

public class AirshipGame extends AndroidGame {
	
	public static final float MAX_SENSITIVITY = 2.0f;
	public static final float MIN_SENSITIVITY = 0.3f;
	public static final float DEFAULT_SENSITIVITY = 1;

    GoogleAPIManager apiManager;
    String moneyLeaderboardID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Preferences.loadPreferences(getPreferences(MODE_PRIVATE), getPreferences(MODE_PRIVATE).edit());
		
		float max = MAX_SENSITIVITY;
		float min = MIN_SENSITIVITY;
        Preferences.DEFAULT_SENSITIVITY_PERCENT = (DEFAULT_SENSITIVITY / (max - min));
        Preferences.resetDefaults();

        apiManager = new GoogleAPIManager(this);
        moneyLeaderboardID = getResources().getString(R.string.leaderboard_money_earned);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Result: " + resultCode, "We need: " + Activity.RESULT_CANCELED);
        if (requestCode == GoogleAPIManager.CONNECTION_RESOLUTION_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            apiManager.connect();
            Log.d("ActivityResult", "Attempting to reconnect");
        } else {
            Log.d("ActivityResult", "Result not okay. Cancelling operation.");
        }
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
	
	public ShipParams loadMerchantShipParams()
	{
		ShipParams params = new ShipParams("Interceptor");
		return params;
	}

    public void pushMoneyScore(int score)
    {
        apiManager.pushLeaderboardScore(moneyLeaderboardID, score);
    }

}
