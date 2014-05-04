package com.mitch.flyship;

import com.mitch.flyship.screens.Loading;
import com.mitch.framework.Screen;
import com.mitch.framework.implementation.AndroidGame;

public class AirshipGame extends AndroidGame {
	
	
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

}
