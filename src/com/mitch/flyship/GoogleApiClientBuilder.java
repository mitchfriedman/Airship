package com.mitch.flyship;

import android.os.Bundle;
import android.view.Gravity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.mitch.framework.implementation.AndroidGame;

public class GoogleApiClientBuilder {
	
	/*public GoogleApiClient client = null;
	public boolean connected = false;
	private boolean pushScore;
	private int scoreToPush;
	public String LEADERBOARD_ID;
	
	public GoogleApiClientBuilder(AndroidGame androidGame) {
		client = buildClient(androidGame);
		LEADERBOARD_ID = androidGame.getResources().getString(R.string.leaderboard_money_earned);
	}

	public GoogleApiClient buildClient(AndroidGame androidGame) {
		return new GoogleApiClient.Builder(androidGame)
        .addApi(Games.API)
        .addScope(Games.SCOPE_GAMES)
        .setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
	    .addConnectionCallbacks(this)
	    .addOnConnectionFailedListener(this)
	    .build();
	}
	
	public GoogleApiClient getClient() {
		return client;
	}
	
	public void connect() {
		client.connect();
	}
	
	public boolean connected() {
		return connected;
	}
	
	public void pushToLeaderBoards(int score) {
        client.
		pushScore = true;
		scoreToPush = score;
		connect();
		//Games.Leaderboards.submitScore(client, LEADERBOARD_ID, 1337);
	}
	
	private void pushConnectedCallback(int score) {
		Games.Leaderboards.submitScore(client, LEADERBOARD_ID, score);
	}

	@Override
	public void onConnected(Bundle arg0) {
		connected = true;
		//if(pushScore) {
			
			pushConnectedCallback(10);
			//pushScore = false;
		//}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		connected = false;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		connected = false;
	}*/


}
